package com.hari.hackies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hari.hackies.R
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.CommentModel
import com.hari.hackies.ui.utils.StringExtensions.capitalizeFirstLetter

class CommentsAdapter(private val storiesInterface: StoriesInterface, private val commentsList:List<CommentModel>?= listOf()):
    RecyclerView.Adapter<CommentsAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorNameHeaderTV = itemView.findViewById<TextView>(R.id.author_name_header_list_comments)!!
        val authorNameTV = itemView.findViewById<TextView>(R.id.author_name_list_comments)!!
        val dateTV = itemView.findViewById<TextView>(R.id.date_list_comments)!!
        val commentTV = itemView.findViewById<TextView>(R.id.comment_list_comments)!!
        val replyTV = itemView.findViewById<TextView>(R.id.reply_count_list_comments)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_comments, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.authorNameHeaderTV.text = commentsList!![position].by!!.capitalizeFirstLetter()
        holder.authorNameTV.text = commentsList[position].by
        holder.dateTV.text = commentsList[position].date // need to convert unix time to date like 10hours ago while getting data from db or saving data to db
        holder.commentTV.text = commentsList[position].text
        holder.replyTV.text = commentsList[position].kids.size.toString()

        holder.replyTV.setOnClickListener {

            if (commentsList.get(position).kids.size > 0)
            storiesInterface.showReplyBottomSheetDialog(commentsList[position], position)
            else Snackbar.make(holder.itemView, "This comment has no reply", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return commentsList!!.size
    }
}