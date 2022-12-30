package com.hari.hackies.ui

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hari.hackies.R
import com.hari.hackies.viewmodel.StoryViewModel
import io.reactivex.disposables.CompositeDisposable

class Splash : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        val splashIcon = findViewById<ImageView>(R.id.app_icon_splash)
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_anim)
        splashIcon.startAnimation(bounceAnimation)

        val viewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        viewModel.initStoryRepo(this, disposable, true, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}