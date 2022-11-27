package com.hari.hackies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.hari.hackies.R
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.ui.utils.StringExtensions.capitalizeFirstLetter

class ReplyAdapter(private val storiesInterface: StoriesInterface, private val replyList:List<ReplyModel>?= listOf()):
    RecyclerView.Adapter<ReplyAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorNameHeaderTV = itemView.findViewById<TextView>(R.id.author_name_header_list_reply)!!
        val authorNameTV = itemView.findViewById<TextView>(R.id.author_name_list_reply)!!
        val dateTV = itemView.findViewById<TextView>(R.id.date_list_reply)!!
        val commentTV = itemView.findViewById<TextView>(R.id.comment_list_reply)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_reply, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.authorNameHeaderTV.text = replyList!![position].by!!.capitalizeFirstLetter()
        holder.authorNameTV.text = replyList[position].by
        holder.dateTV.text = replyList[position].date // need to convert unix time to date like 10hours ago while getting data from db or saving data to db
        holder.commentTV.text = HtmlCompat.fromHtml(replyList[position].text!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun getItemCount(): Int {
        return replyList!!.size
    }
}