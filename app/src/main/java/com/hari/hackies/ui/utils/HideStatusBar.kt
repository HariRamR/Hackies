package com.hari.hackies.ui.utils

import android.app.Activity
import android.view.Window
import android.view.WindowManager

object HideStatusBar {

    fun hideStatusBar(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}