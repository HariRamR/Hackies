package com.hari.hackies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.hari.hackies.R
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.ui.utils.StringExtensions.capitalizeFirstLetter

class ReplyAdapter: RecyclerView.Adapter<ReplyAdapter.MainViewHolder>() {

    private val regex = Regex("[^A-Za-z0-9 ]")
    private var replyList:List<CommentModel> = listOf()

    fun setReplyData(replyList:List<CommentModel>){
        this.replyList = replyList
        notifyDataSetChanged()
    }

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

        if (replyList[position].deleted!! || replyList[position].dead!!){

            holder.authorNameHeaderTV.text = "D"
            holder.authorNameTV.visibility = View.GONE
            holder.dateTV.text = replyList[position].date
            holder.commentTV.text = holder.itemView.context
                .getString(if(replyList[position].deleted!!) R.string.comment_deleted_txt
                else R.string.comment_not_active_txt)
        }else{

            holder.authorNameTV.visibility = View.VISIBLE
            var headerTxt = replyList[position].by.toString()
            headerTxt = regex.replace(headerTxt, "")
            holder.authorNameHeaderTV.text = headerTxt.capitalizeFirstLetter()

            holder.authorNameTV.text = replyList[position].by
            holder.dateTV.text = replyList[position].date
            holder.commentTV.text = HtmlCompat.fromHtml(replyList[position].text!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun getItemCount(): Int {
        return replyList.size
    }
}