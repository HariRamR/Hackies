package com.hari.hackies.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hari.hackies.R
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.Dashboard
import com.hari.hackies.ui.fragment.StoryFrag
import com.hari.hackies.ui.utils.StringExtensions.capitalizeFirstLetter
import java.util.*

open class StoriesAdapter(private val storiesInterface: StoriesInterface):
    RecyclerView.Adapter<StoriesAdapter.MainViewHolder>(), Filterable {

    private val regex = Regex("[^A-Za-z0-9 ]")
    private var storiesList:List<StoryModel>?= listOf()
    private var filteredData: List<StoryModel>?= listOf()

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

        if (filteredData!![position].deleted!! || filteredData!![position].dead!!){
            holder.storyTitleHeaderTV.text = "D"
            holder.storyTitleTV.visibility = View.GONE
            holder.authorNameTV.text = holder.itemView.context
                .getString(if(filteredData!![position].deleted!!) R.string.story_deleted_txt
                else R.string.story_not_active_txt)
            holder.scoreTV.visibility = View.GONE
            holder.commentsCountTV.visibility = View.GONE
            holder.dateTV.text = filteredData!![position].date
        }else{

            holder.storyTitleTV.visibility = View.VISIBLE
            holder.scoreTV.visibility = View.VISIBLE
            holder.commentsCountTV.visibility = View.VISIBLE

            var headerTxt = filteredData!![position].title.toString()
            headerTxt = regex.replace(headerTxt, "")
            holder.storyTitleHeaderTV.text = headerTxt.capitalizeFirstLetter()
            val drawable = holder.storyTitleHeaderTV.background.mutate()
            DrawableCompat.setTint(drawable, filteredData!![position].nameBGClr!!)
            holder.storyTitleHeaderTV.background = drawable
            holder.storyTitleTV.text = filteredData!![position].title!!.capitalizeFirstLetter()
            holder.authorNameTV.text = filteredData!![position].by
            holder.dateTV.text = filteredData!![position].date
            holder.scoreTV.text = if(filteredData!![position].score != null) filteredData!![position].score.toString() else "0"
            holder.commentsCountTV.text = String.format("%s%s", "Comments  ", filteredData!![position].kids.size)

            holder.storyTitleTV.setOnClickListener {
                if (filteredData!![position].url != null && filteredData!![position].url!!.isNotEmpty()){

                    val fragment = StoryFrag()
                    val fragmentTransaction =
                        (holder.itemView.context as Dashboard).supportFragmentManager.beginTransaction()

                    val bundle = Bundle()
                    bundle.putString("StoryURL", filteredData!![position].url)
                    fragment.arguments = bundle
                    fragment.show(fragmentTransaction, "StoryFrag")
                }else Snackbar.make(holder.itemView, holder.itemView.context.resources.getString(R.string.no_web_link_found), Snackbar.LENGTH_SHORT).show()
            }

            holder.commentsCountTV.setOnClickListener {

                if (filteredData!![position].kids.size > 0){

                    notifyItemChanged(position)
                    storiesInterface.showCommentsBottomSheetDialog(filteredData!![position], position)
                }

                else Snackbar.make(holder.itemView, holder.itemView.context.resources.getString(R.string.no_comments_found), Snackbar.LENGTH_SHORT).show()
            }
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