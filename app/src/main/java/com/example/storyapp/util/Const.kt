package com.example.storyapp.util

import java.lang.Boolean
import kotlin.Long

object Const {

    const val TIME_SPLASH : Long = 5000
    const val ACTION_DELAYED_TIME : Long = 1000

    const val KEY_TOKEN = "key.token"
    const val KEY_USER_ID = "key.user.id"
    const val KEY_USER_NAME = "key.user.name"
    const val KEY_IS_LOGIN = "key.isLogin"
    const val KEY_EMAIL = "key.email"
    const val PREFS_NAME = "storyapp.pref"
    val DEBUG = Boolean.parseBoolean("true")

    const val REQUEST_CODE_PERMISSIONS = 10
    const val CAMERA_X_RESULT = 200
    const val KEY_PICTURE = "picture"
    const val KEY_IS_BACK_CAMERA = "isBackCamera"


    const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    const val LOGIN_URL = "login"
    const val REGISTER_URL = "register"
    const val UPLOAD = "stories"
    const val UPLOAD_GUEST = "stories/guest"
    const val GET = "stories"

    const val BUNDLE_KEY_STORY = "bundle.story"


}