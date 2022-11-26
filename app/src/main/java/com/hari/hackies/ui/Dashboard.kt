package com.hari.hackies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hari.hackies.R
import com.hari.hackies.common.HideStatusBar.hideStatusBar

class Dashboard: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        hideStatusBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


    }
}