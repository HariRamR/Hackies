package com.hari.hackies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hari.hackies.R
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.StoriesModel
import java.util.ArrayList

class StoriesAdapter(private val storiesInterface: StoriesInterface, private val storiesList:List<StoriesModel>?= listOf()):
    RecyclerView.Adapter<StoriesAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentsTV = itemView.findViewById<TextView>(R.id.comments_list_stories)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_stories, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.commentsTV.setOnClickListener {
            storiesInterface.showBottomSheetDialog(0) // pass id of story here
        }
    }

    override fun getItemCount(): Int {
        return storiesList!!.size
    }
}