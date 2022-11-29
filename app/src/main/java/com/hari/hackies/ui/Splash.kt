package com.hari.hackies.ui

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hari.hackies.R
import com.hari.hackies.viewmodel.StoryViewModel
import io.reactivex.disposables.CompositeDisposable

class Splash : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {

//        hideStatusBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        val splashNameTV = findViewById<TextView>(R.id.hackies_txt_splash)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_anim)
        splashNameTV.startAnimation(slideAnimation)

        val viewModel = StoryViewModel.getInstance(application)
        viewModel.initStoryRepo(this, disposable, true, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}