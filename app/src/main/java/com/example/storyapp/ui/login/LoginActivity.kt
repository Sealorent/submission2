package com.example.storyapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.data.Resource
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.util.Const
import com.example.storyapp.util.ViewStateCallback

class LoginActivity : AppCompatActivity() ,ViewStateCallback<LoginResponse> {
    private lateinit var loginBinding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        LoginButton()
        signUp()
    }


    private fun signUp(){
        loginBinding.toDaftar.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun LoginButton() {
        loginBinding.login.setOnClickListener {
            val email = loginBinding.edtEmail.text.toString()
            val password = loginBinding.edtPassword.text.toString()
            closeKeyboard()
            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    email.isBlank() -> loginBinding.edtEmail.error = getString(R.string.error_empty_email)
                    password.isBlank() -> loginBinding.edtPassword.error = getString(R.string.error_empty_password)
                    else -> {
                        viewModel.signIn( email,password).observe(this) {
                            when (it) {
                                is Resource.Error -> onFailed(it.message.toString())
                                is Resource.Loading -> onLoading()
                                is Resource.Success -> it.data?.let { it1 -> onSuccess(it1) }
                            }
                        }
                    }
                }
            }, Const.ACTION_DELAYED_TIME)

        }
    }

    override fun onSuccess(data: LoginResponse) {
        loginBinding.apply {
            progressBar.visibility = invisible
            textError.visibility = invisible
            val data = data
            try {
                Toast.makeText(this@LoginActivity, data.message, Toast.LENGTH_SHORT).show()
            }finally {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onLoading() {
    loginBinding.apply {
        progressBar.visibility = visible
        textError.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {

        loginBinding.apply {
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

