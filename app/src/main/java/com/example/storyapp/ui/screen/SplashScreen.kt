package com.example.storyapp.ui.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.storyapp.MainActivity
import com.example.storyapp.databinding.ActivitySplashScreenBinding
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.util.Const.TIME_SPLASH
import com.example.storyapp.util.SessionManager


class SplashScreen : AppCompatActivity() {

    private lateinit var splashBinding: ActivitySplashScreenBinding
    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        val handler = Handler(mainLooper)
        pref = SessionManager(this)
        val isLogin = pref.isLogin
        handler.postDelayed({
            when {
                isLogin -> {
                    MainActivity.start(this)
                    finish()
                }
                else -> {
                    val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }, TIME_SPLASH)
    }
}