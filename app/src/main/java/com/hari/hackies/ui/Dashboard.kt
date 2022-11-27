package com.hari.hackies.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hari.hackies.R
import com.hari.hackies.adapter.CommentsAdapter
import com.hari.hackies.adapter.ReplyAdapter
import com.hari.hackies.adapter.StoriesAdapter
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.utils.DisplaySize
import com.hari.hackies.ui.utils.HideStatusBar.hideStatusBar

class Dashboard: AppCompatActivity(), StoriesInterface {

    private lateinit var commentBottomSheetDialog: BottomSheetDialog
    private lateinit var replyBottomSheetDialog: BottomSheetDialog
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var replyAdapter: ReplyAdapter
    private lateinit var backBtnReply: ImageView
    private lateinit var authorNameHeaderTVReply: TextView
    private lateinit var authorNameTVReply: TextView
    private lateinit var dateTVReply: TextView
    private lateinit var commentTVReply: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        hideStatusBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.hide()

        initViews()
    }

    private fun initViews(){

        commentBottomSheetDialog = BottomSheetDialog(this)
        replyBottomSheetDialog = BottomSheetDialog(this)
        commentBottomSheetDialog.setContentView(R.layout.bottom_sheet_comments)
        replyBottomSheetDialog.setContentView(R.layout.bottom_sheet_reply)
        val bottomSheetHeight = DisplaySize.getDisplaySize(this, true, 2).toInt()
        commentBottomSheetDialog.behavior.maxHeight = bottomSheetHeight
        replyBottomSheetDialog.behavior.maxHeight = bottomSheetHeight

        backBtnReply = replyBottomSheetDialog.findViewById(R.id.back_bottom_sheet_reply)!!
        authorNameHeaderTVReply = replyBottomSheetDialog.findViewById(R.id.author_name_header_bottom_sheet_reply)!!
        authorNameTVReply = replyBottomSheetDialog.findViewById(R.id.author_name_bottom_sheet_reply)!!
        dateTVReply = replyBottomSheetDialog.findViewById(R.id.date_bottom_sheet_reply)!!
        commentTVReply = replyBottomSheetDialog.findViewById(R.id.comment_bottom_sheet_reply)!!

        val storiesRecycler = findViewById<RecyclerView>(R.id.recycler_dashboard)
        storiesAdapter = StoriesAdapter(this, listOf(createDummyStoryModel(), createDummyStoryModel2(), createDummyStoryModel(), createDummyStoryModel2()))
        storiesRecycler!!.layoutManager = LinearLayoutManager(this)
        storiesRecycler.adapter = storiesAdapter

        val commentsRecycler = commentBottomSheetDialog.findViewById<RecyclerView>(R.id.comments_recycler_bottom_sheet)
        commentsAdapter = CommentsAdapter(this, listOf(createDummyCommentModel(), createDummyCommentModel(), createDummyCommentModel()))
        commentsRecycler!!.layoutManager = LinearLayoutManager(this)
        commentsRecycler.adapter = commentsAdapter

        val replyRecycler = replyBottomSheetDialog.findViewById<RecyclerView>(R.id.comments_recycler_bottom_sheet_reply)
        replyAdapter = ReplyAdapter(this, listOf(createDummyReplyModel(), createDummyReplyModel()))
        replyRecycler!!.layoutManager = LinearLayoutManager(this)
        replyRecycler.adapter = replyAdapter

        val search = findViewById<EditText>(R.id.search_dashboard)
        search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                storiesAdapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })

        backBtnReply.setOnClickListener {
            replyBottomSheetDialog.dismiss()
        }
    }

    override fun showCommentsBottomSheetDialog(id: Int, pos: Int) {
        // check for comment size and if > 0 and comments table has no data then fetch from api
        if(!commentBottomSheetDialog.isShowing){
            commentBottomSheetDialog.show()
        }
    }

    override fun showReplyBottomSheetDialog(commentModel: CommentModel, pos: Int){
        // check for comment size and if > 0 and comments table has no data then fetch from api
        if(!replyBottomSheetDialog.isShowing){

            authorNameHeaderTVReply.text = commentModel.by
            authorNameTVReply.text = commentModel.by
            dateTVReply.text = commentModel.date
            commentTVReply.text = commentModel.text

            replyBottomSheetDialog.show()
        }
    }

    fun createDummyStoryModel(): StoryModel{
        return StoryModel("hari ram", 0, 123412, arrayListOf(122, 2133, 1223), 100, 1669394133,
            "10 hours ago", "The Insane Scale of Europe’s New Mega-Tunnel", "story",
            "https://www.youtube.com/watch?v=QiYvXKQksgI")
    }
    fun createDummyStoryModel2(): StoryModel{
        return StoryModel("Vinoth", 0, 123412, arrayListOf(122, 2133, 1223), 100, 1669394133,
            "1 year ago", "Researcher: Apple’s pseudo-VPN abused for ad fraud", "story",
            "https://techaint.com/2022/11/23/researcher-apples-pseudo-vpn-abused-for-ad-fraud/")
    }
    fun createDummyCommentModel(): CommentModel{
        return CommentModel("Latha", 123412, arrayListOf(122, 2133), 123412, "It has been around 17 years now. It still amazes me that so much high quality and informative content like this is made on a near daily basis",
            1669394133,  "5 hours ago",
            "comment")
    }
    fun createDummyReplyModel(): ReplyModel{
        return ReplyModel("ramasamy", 123412, arrayListOf(122, 2133), 123412, "you are right!!",
            1669394133,  "2 hours ago",
            "comment")
    }
}