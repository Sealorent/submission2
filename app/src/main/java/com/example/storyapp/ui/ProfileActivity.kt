package com.example.storyapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.databinding.ActivityProfileBinding
import com.example.storyapp.util.SessionManager
import com.example.storyapp.R.string
import com.example.storyapp.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding
    private lateinit var pref : SessionManager

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        pref = SessionManager(this)
        initUI()
        initAction()

    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(string.title_profile)

        profileBinding.tvUserName.text = pref.getUserName
        profileBinding.tvUserEmail.text = pref.getEmail
    }

    private fun initAction() {
        profileBinding.btnLogout.setOnClickListener {
            openLogoutDialog()
        }
    }

    private fun openLogoutDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(string.message_logout_confirm))
            ?.setPositiveButton(getString(string.action_yes)) { _, _ ->
                pref.clearPreferences()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            ?.setNegativeButton(getString(string.action_cancel), null)
        val alert = alertDialog.create()
        alert.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}