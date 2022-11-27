package com.hari.hackies.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.hari.hackies.R
import com.hari.hackies.ui.utils.HideStatusBar.hideStatusBar

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        hideStatusBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }, 5000)


    }
}