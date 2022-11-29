package com.hari.hackies.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hari.hackies.R
import com.hari.hackies.adapter.CommentsAdapter
import com.hari.hackies.adapter.ReplyAdapter
import com.hari.hackies.adapter.StoriesAdapter
import com.hari.hackies.interfaces.StoriesInterface
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.utils.CheckInternet
import com.hari.hackies.ui.utils.ConvertTime
import com.hari.hackies.ui.utils.DisplaySize
import com.hari.hackies.viewmodel.CommentViewModel
import com.hari.hackies.viewmodel.ReplyViewModel
import com.hari.hackies.viewmodel.StoryViewModel
import io.reactivex.disposables.CompositeDisposable

class Dashboard: AppCompatActivity(), StoriesInterface, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var commentBottomSheetDialog: BottomSheetDialog
    private lateinit var replyBottomSheetDialog: BottomSheetDialog
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var replyAdapter: ReplyAdapter
    private lateinit var rootRelative: RelativeLayout
    private lateinit var backBtnReply: ImageView
    private lateinit var authorNameHeaderTVReply: TextView
    private lateinit var authorNameTVReply: TextView
    private lateinit var dateTVReply: TextView
    private lateinit var commentTVReply: TextView
    private lateinit var errorTV: TextView
    private lateinit var noDataTV: TextView
    private lateinit var commentProgressBar: ProgressBar
    private lateinit var replyProgressBar: ProgressBar
    private lateinit var viewModel: StoryViewModel
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var replyViewModel: ReplyViewModel
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

        rootRelative = findViewById(R.id.root_relative_dashboard)!!
        commentProgressBar = commentBottomSheetDialog.findViewById(R.id.progress_bottom_sheet_comments)!!
        replyProgressBar = replyBottomSheetDialog.findViewById(R.id.progress_bottom_sheet_reply)!!
        backBtnReply = replyBottomSheetDialog.findViewById(R.id.back_bottom_sheet_reply)!!
        authorNameHeaderTVReply = replyBottomSheetDialog.findViewById(R.id.author_name_header_bottom_sheet_reply)!!
        authorNameTVReply = replyBottomSheetDialog.findViewById(R.id.author_name_bottom_sheet_reply)!!
        dateTVReply = replyBottomSheetDialog.findViewById(R.id.date_bottom_sheet_reply)!!
        commentTVReply = replyBottomSheetDialog.findViewById(R.id.comment_bottom_sheet_reply)!!

        val storiesRecycler = findViewById<RecyclerView>(R.id.recycler_dashboard)
        storiesAdapter = StoriesAdapter(this)
        storiesRecycler!!.layoutManager = LinearLayoutManager(this)
        storiesRecycler.adapter = storiesAdapter

        val commentsRecycler = commentBottomSheetDialog.findViewById<RecyclerView>(R.id.recycler_bottom_sheet_comments)
        commentsAdapter = CommentsAdapter(this)
        commentsRecycler!!.layoutManager = LinearLayoutManager(this)
        commentsRecycler.adapter = commentsAdapter

        val replyRecycler = replyBottomSheetDialog.findViewById<RecyclerView>(R.id.comments_recycler_bottom_sheet_reply)
        replyAdapter = ReplyAdapter()
        replyRecycler!!.layoutManager = LinearLayoutManager(this)
        replyRecycler.adapter = replyAdapter
        replyRecycler.isNestedScrollingEnabled = false

        errorTV = findViewById(R.id.error_dashboard)
        noDataTV = findViewById(R.id.no_data_dashboard)
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
        commentViewModel = CommentViewModel.getInstance(application)
        replyViewModel = ReplyViewModel.getInstance(application)
        viewModel.getStoriesData().observe(this, Observer {

                for(story in it){
                    val date = ConvertTime.format(story.time!!)
                    story.date = date
                }
                storiesAdapter.setStoryData(it)
        })

        viewModel.isNoData().observe(this, Observer {
            if (it){
                if (CheckInternet.isInternetAvailable(this)){
                    storiesRecycler.visibility = View.GONE
                    noDataTV.visibility = View.VISIBLE
                }else {
                    storiesRecycler.visibility = View.GONE
                    errorTV.visibility = View.VISIBLE
                    commentViewModel.isNetAvailable.value = false

                }
            }else{
                noDataTV.visibility = View.GONE
                errorTV.visibility = View.GONE
                storiesRecycler.visibility = View.VISIBLE
            }
        })

        commentBottomSheetDialog.setOnDismissListener {
            storiesAdapter.notifyItemChanged(selectedStoryPos!!)
        }

        commentViewModel.isNetAvailable().observe(this, Observer {
            if(!it){

                Snackbar.make(rootRelative, "Check your internet connection and try again", Snackbar.LENGTH_SHORT).show()
                commentViewModel.isNetAvailable.value = true
            }
        })

        //comment observer
        commentViewModel.getCommentData().observe(this, Observer {

            for(comment in it){
                val date = ConvertTime.format(comment.time!!)
                comment.date = date
            }
            commentsAdapter.setCommentData(it)
            commentBottomSheetDialog.show()
        })

        commentViewModel.isCommentLoading().observe(this, Observer {
            if(it)
                commentProgressBar.visibility = View.VISIBLE
            else
                commentProgressBar.visibility = View.GONE
        })

        //Reply observer
        replyViewModel.getReplyData().observe(this, Observer {

            for(reply in it){
                val date = ConvertTime.format(reply.time!!)
                reply.date = date
            }
            replyAdapter.setReplyData(it)
            replyBottomSheetDialog.show()
        })

        replyViewModel.isReplyLoading().observe(this, Observer {

            if(it)
                replyProgressBar.visibility = View.VISIBLE
            else
                replyProgressBar.visibility = View.GONE
        })
    }

    override fun showCommentsBottomSheetDialog(storyModel: StoryModel, pos: Int) {

        if(!commentBottomSheetDialog.isShowing){

            selectedStoryPos = pos
            storyModel.isSelected = false
            commentViewModel.initCommentRepo(disposable, storyModel.kids, storyModel.id!!, context = this)
        }
    }

    override fun showReplyBottomSheetDialog(commentModel: CommentModel, pos: Int){

        if(!replyBottomSheetDialog.isShowing){

            authorNameHeaderTVReply.text = commentModel.by
            authorNameTVReply.text = commentModel.by
            dateTVReply.text = commentModel.date
            commentTVReply.text = HtmlCompat.fromHtml(commentModel.text!!, HtmlCompat.FROM_HTML_MODE_LEGACY)

            replyViewModel.initCommentRepo(disposable, commentModel.kids, commentModel.id!!, this)
        }
    }

    override fun onRefresh() {

        viewModel.isLoading().observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it
        })
        viewModel.isLoading.value = true
        viewModel.initStoryRepo(this, disposable, false, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}