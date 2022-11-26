package com.hari.hackies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hari.hackies.R
import com.hari.hackies.adapter.StoriesAdapter
import com.hari.hackies.common.HideStatusBar.hideStatusBar
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.StoriesModel

class Dashboard: AppCompatActivity(), StoriesInterface {

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        hideStatusBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.hide()
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_comments)

        val commentsRecycler = bottomSheetDialog.findViewById<RecyclerView>(R.id.comments_recycler_bottom_sheet)
        // create stories adapter

        val storiesRecycler = findViewById<RecyclerView>(R.id.recycler_dashboard)
        val storiesAdapter = StoriesAdapter(this, listOf(StoriesModel(), StoriesModel(), StoriesModel(), StoriesModel()))
        storiesRecycler!!.layoutManager = LinearLayoutManager(this)
        storiesRecycler.adapter = storiesAdapter


    }

    override fun showBottomSheetDialog(id: Int) {
        // check for comment size and if > 0 and comments table has no data then fetch from api
        if(!bottomSheetDialog.isShowing)
            bottomSheetDialog.show()
    }
}