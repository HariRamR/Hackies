package com.hari.hackies.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hari.hackies.R
import com.hari.hackies.adapter.CommentsAdapter
import com.hari.hackies.adapter.ReplyAdapter
import com.hari.hackies.adapter.StoriesAdapter
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.utils.ConvertTime
import com.hari.hackies.ui.utils.DisplaySize
import com.hari.hackies.viewmodel.CommentViewModel
import com.hari.hackies.viewmodel.StoryViewModel
import io.reactivex.disposables.CompositeDisposable


class Dashboard: AppCompatActivity(), StoriesInterface, SwipeRefreshLayout.OnRefreshListener {

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
    private lateinit var errorTV: TextView
    private lateinit var viewModel: StoryViewModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val disposable = CompositeDisposable()
    private var selectedStoryPos: Int?= 0

    override fun onCreate(savedInstanceState: Bundle?) {

//        hideStatusBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.hide()

        initViews()
    }

    private fun initViews(){

        swipeRefreshLayout = findViewById(R.id.swipe_layput_dashboard)
        swipeRefreshLayout.setOnRefreshListener(this)
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
        storiesAdapter = StoriesAdapter(this)
        storiesRecycler!!.layoutManager = LinearLayoutManager(this)
        storiesRecycler.adapter = storiesAdapter

        val commentsRecycler = commentBottomSheetDialog.findViewById<RecyclerView>(R.id.comments_recycler_bottom_sheet)
        commentsAdapter = CommentsAdapter(this)
        commentsRecycler!!.layoutManager = LinearLayoutManager(this)
        commentsRecycler.adapter = commentsAdapter

        val replyRecycler = replyBottomSheetDialog.findViewById<RecyclerView>(R.id.comments_recycler_bottom_sheet_reply)
        replyAdapter = ReplyAdapter(this)
        replyRecycler!!.layoutManager = LinearLayoutManager(this)
        replyRecycler.adapter = replyAdapter

        errorTV = findViewById(R.id.error_dashboard)
        val moveToTopBtn = findViewById<FloatingActionButton>(R.id.move_to_top_dashboard)
        moveToTopBtn.setOnClickListener {
            val layoutManager: LinearLayoutManager = storiesRecycler.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(0, 0)
        }

        val search = findViewById<EditText>(R.id.search_dashboard)
        search.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                storiesAdapter.filter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        backBtnReply.setOnClickListener {
            replyBottomSheetDialog.dismiss()
        }

        viewModel = StoryViewModel.getInstance(application)
        viewModel.getStoriesData().observe(this, Observer {

                for(story in it){
                    val date = ConvertTime.format(story.time!!)
                    story.date = date
                }
                storiesAdapter.setStoryData(it)
        })
        viewModel.isError().observe(this, Observer {
            if (it){
                storiesRecycler.visibility = View.GONE
                errorTV.visibility = View.VISIBLE
            }else{
                errorTV.visibility = View.GONE
                storiesRecycler.visibility = View.VISIBLE
            }
        })

        commentBottomSheetDialog.setOnDismissListener {
            storiesAdapter.notifyItemChanged(selectedStoryPos!!)
        }
    }

    override fun showCommentsBottomSheetDialog(storyModel: StoryModel, pos: Int) {

        if(!commentBottomSheetDialog.isShowing){

            selectedStoryPos = pos
            storyModel.isSelected = false
            val viewModel = CommentViewModel.getInstance(application)
            viewModel.initCommentRepo(disposable, storyModel)

            viewModel.getCommentsData().observe(this, Observer {

                for(comment in it){
                    val date = ConvertTime.format(comment.time!!)
                    comment.date = date
                }
                commentsAdapter.setCommentData(it)
            })

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

    override fun onRefresh() {

        viewModel.isLoading().observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it
        })
        viewModel.isLoading.value = true
        viewModel.initStoryRepo(this, disposable, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}