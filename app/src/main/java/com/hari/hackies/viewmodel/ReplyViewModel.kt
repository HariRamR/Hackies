package com.hari.hackies.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hari.hackies.database.StoryDAO
import com.hari.hackies.database.StoryDatabase
import com.hari.hackies.model.CommentModel
import com.hari.hackies.repository.CommentRepo
import io.reactivex.disposables.CompositeDisposable

class ReplyViewModel: ViewModel() {

    var replyListList: MutableLiveData<List<CommentModel>> = MutableLiveData<List<CommentModel>>()
    private var commentRepo: CommentRepo?= null
    var isReplyLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    companion object{

        private var database: StoryDatabase?= null
        var dao: StoryDAO?= null

        private var replyViewModel: ReplyViewModel?= null
        private var application: Application?= null

        fun getInstance(application: Application): ReplyViewModel {

            if (replyViewModel == null){
                replyViewModel = ReplyViewModel()
                this.application = application
                database = StoryDatabase.getInstance(application)
                dao = database!!.getStoryDAO()
            }
            return replyViewModel!!
        }
    }

    fun initCommentRepo(disposable: CompositeDisposable, kids: ArrayList<Int>, id: Int, context: Context){
        commentRepo = CommentRepo(disposable, commentList =  replyListList, isLoading =  isReplyLoading)
        commentRepo!!.checkAndDownloadComments(kids, id, context)
    }

    fun isReplyLoading(): LiveData<Boolean> {
        return isReplyLoading
    }

    fun getReplyData(): LiveData<List<CommentModel>> {
        return replyListList
    }
}