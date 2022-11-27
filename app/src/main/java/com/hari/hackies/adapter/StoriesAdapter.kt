package com.hari.hackies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hari.hackies.R
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.utils.StringExtensions.capitalizeFirstLetter
import java.util.*
import kotlin.collections.ArrayList

open class StoriesAdapter(private val storiesInterface: StoriesInterface):
    RecyclerView.Adapter<StoriesAdapter.MainViewHolder>(), Filterable {

    private var storiesList:List<StoryModel>?= null
    private var filteredData: List<StoryModel>?= null

    fun setStoryData(storiesList: List<StoryModel>){
        this.storiesList = storiesList
        this.filteredData = storiesList
        notifyDataSetChanged()
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storyTitleHeaderTV = itemView.findViewById<TextView>(R.id.story_title_header_list_stories)!!
        val storyTitleTV = itemView.findViewById<TextView>(R.id.story_title_list_stories)!!
        val authorNameTV = itemView.findViewById<TextView>(R.id.author_list_stories)!!
        val dateTV = itemView.findViewById<TextView>(R.id.date_list_stories)!!
        val commentsCountTV = itemView.findViewById<TextView>(R.id.comments_list_stories)!!
        val scoreTV = itemView.findViewById<TextView>(R.id.score_list_stories)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_stories, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.storyTitleHeaderTV.text = filteredData!![position].title!!.capitalizeFirstLetter()
        holder.storyTitleTV.text = filteredData!![position].title!!.capitalizeFirstLetter()
        holder.authorNameTV.text = filteredData!![position].by
        holder.dateTV.text = filteredData!![position].date // need to convert unix time to date like 10hours ago while getting data from db or saving data to db
        holder.scoreTV.text = if(filteredData!![position].score != null) filteredData!![position].score.toString() else "0"
        holder.commentsCountTV.text = String.format("%s%s", "Comments  ", filteredData!![position].kids.size)

        holder.commentsCountTV.setOnClickListener {

            if (filteredData!![position].kids.size > 0)
                storiesInterface.showCommentsBottomSheetDialog(filteredData!![position].id!!, position)
            else Snackbar.make(holder.itemView, "This story has no comments", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return filteredData!!.size
    }

    override fun getFilter(): Filter {

        return object: Filter() {

            override fun performFiltering(constraint: CharSequence): FilterResults {

                val filterString = constraint.toString().lowercase(Locale.getDefault())
                val results = FilterResults()
                val list: List<StoryModel> = storiesList!!
                val count = list.size
                val nlist = ArrayList<StoryModel>(count)
                var title: String
                var author: String
                for (i in 0 until count) {
                    title = list[i].title!!
                    author = list[i].by!!
                    if (title.lowercase(Locale.getDefault()).contains(filterString) || author.lowercase(Locale.getDefault()).contains(filterString)) {
                        nlist.add(list[i])
                    }
                }
                results.values = nlist
                results.count = nlist.size
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {

                filteredData = if (results.values == null)
                    arrayListOf()
                else
                    results.values as ArrayList<StoryModel>
                notifyDataSetChanged()
            }
        }
    }
}