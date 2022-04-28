package com.example.storyapp.ui.story.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.storyapp.MainActivity
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.util.Const.REQUEST_CODE_PERMISSIONS
import com.example.storyapp.util.SessionManager
import okhttp3.MultipartBody
import java.io.File
import com.example.storyapp.R.string
import com.example.storyapp.data.Resource
import com.example.storyapp.data.response.AddStoriesResponse
import com.example.storyapp.ui.camera.CameraActivity
import com.example.storyapp.util.Const.CAMERA_X_RESULT
import com.example.storyapp.util.Const.KEY_PICTURE
import com.example.storyapp.util.ViewStateCallback
import com.example.storyapp.util.ext.reduceFileImage
import com.example.storyapp.util.ext.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class AddStoryActivity : AppCompatActivity() , ViewStateCallback<AddStoriesResponse> {

    private lateinit var  addStoryBinding : ActivityAddStoryBinding
    private val storyViewModel: StoryViewModel by viewModels()
    private var uploadFile: File? = null
    private var token: String? = null
    private lateinit var pref: SessionManager

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddStoryActivity::class.java)
            context.startActivity(intent)
        }

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding.root)

        pref = SessionManager(this)
        token = pref.getToken

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        initUI()
        initAction()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!allPermissionsGranted()) {
            Toast.makeText(this@AddStoryActivity, getString(string.message_not_permitted),Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun initAction() {
        addStoryBinding.btnOpenCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launchIntentCamera.launch(intent)
        }
        addStoryBinding.btnOpenGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, getString(string.title_choose_a_picture))
            launchIntentGallery.launch(chooser)
        }
        addStoryBinding.btnUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun initUI() {
        title = getString(string.title_new_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val file = it?.data?.getSerializableExtra(KEY_PICTURE) as File

            uploadFile = file

            val result = BitmapFactory.decodeFile(file.path)

            addStoryBinding.imgPreview.setImageBitmap(result)
        }
    }

    private val launchIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val file = uriToFile(selectedImg, this)

            uploadFile = file
            addStoryBinding.imgPreview.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (uploadFile != null) {
            val file = reduceFileImage(uploadFile as File)
            val description = addStoryBinding.edtStoryDesc.text
            if (description.isBlank()) {
                addStoryBinding.edtStoryDesc.requestFocus()
                addStoryBinding.edtStoryDesc.error = getString(string.error_desc_empty)
            } else {
                val descMediaTyped =
                    description.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                storyViewModel.addStories(token.toString(), imageMultipart, descMediaTyped)
                    .observe(this) {
                        when (it) {
                            is Resource.Error -> onFailed(it.message)
                            is Resource.Loading -> onLoading()
                            is Resource.Success -> it.data?.let { it1 ->
                                onSuccess(it1)
                            }
                        }
                    }
            }
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                getString(string.title_message) + getString(string.message_pick_image),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSuccess(data: AddStoriesResponse) {
        var res = data
        addStoryBinding.apply {
            progressBar.visibility = invisible
            bgDim.visibility = invisible
        }
        Toast.makeText(this, "Upload Berhasil", Toast.LENGTH_SHORT).show()
        MainActivity.start(this)
        finish()
    }

    override fun onLoading() {
        addStoryBinding.apply {
            progressBar.visibility = visible
            bgDim.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}