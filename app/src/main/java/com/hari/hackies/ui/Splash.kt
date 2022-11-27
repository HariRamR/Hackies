package com.hari.hackies.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Observer
import com.hari.hackies.R
import com.hari.hackies.ui.utils.HideStatusBar.hideStatusBar
import com.hari.hackies.viewmodel.StoriesViewModel

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        hideStatusBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            startActivity(Intent(this, Dashboard::class.java))
//            finish()
//        }, 5000)

        val viewModel = StoriesViewModel(application)
        viewModel.isLoading().observe(this, Observer {
            if (!it){
                Log.e("IsLoading", "False")
            }
        })

    }
}