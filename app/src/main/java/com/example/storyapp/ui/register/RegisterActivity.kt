package com.example.storyapp.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.R.string
import com.example.storyapp.data.Resource
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.util.Const
import com.example.storyapp.util.ViewStateCallback
import com.example.storyapp.util.ext.isEmailValid


class RegisterActivity : AppCompatActivity() ,ViewStateCallback<LoginResponse>{
    private lateinit var registerBinding : ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        registerBinding.apply {
            progressBar.visibility = invisible
            textError.visibility = invisible
        }

        registerUser()
    }


    private fun registerUser(){
        registerBinding.daftar.setOnClickListener{
            val userName = registerBinding.edtName.text.toString()
            val userEmail = registerBinding.edtEmail.text.toString()
            val userPassword = registerBinding.edtPassword.text.toString()
            closeKeyboard()

            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    userName.isBlank() -> registerBinding.edtName.error = getString(string.error_empty_name)
                    userEmail.isBlank() -> registerBinding.edtEmail.error = getString(string.error_empty_email)
                    !userEmail.isEmailValid() -> registerBinding.edtEmail.error = getString(string.error_invalid_email)
                    userPassword.isBlank() -> registerBinding.edtPassword.error = getString(string.error_empty_password)
                    else -> {
                        viewModel.signUp(userName,userEmail,userPassword).observe(this){
                            when(it){
                                is Resource.Error -> onFailed(it.message)
                                is Resource.Loading -> onLoading()
                                is Resource.Success -> it.data?.let{ it1 ->onSuccess(it1) }
                            }
                        }
                    }
                }
            }, Const.ACTION_DELAYED_TIME)
        }
    }



    override fun onSuccess(data: LoginResponse) {
        registerBinding.apply {
            progressBar.visibility = invisible
            textError.visibility = invisible
            val data = data
            try {
                Toast.makeText(this@RegisterActivity,data.message, Toast.LENGTH_SHORT).show()
            }finally {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onLoading() {
        registerBinding.apply {
            progressBar.visibility = visible
            textError.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        registerBinding.apply {
            progressBar.visibility = invisible
            if (message == null){
                textError.visibility = invisible
            }else{
                textError.visibility = visible
            }
        }
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }

}
