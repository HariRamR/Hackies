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

class CommentViewModel: ViewModel() {

    var commentList: MutableLiveData<List<CommentModel>> = MutableLiveData<List<CommentModel>>()
    var replyListList: MutableLiveData<List<CommentModel>> = MutableLiveData<List<CommentModel>>()
    private var commentRepo: CommentRepo?= null
    var isCommentLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    var isReplyLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    var isNetAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    companion object{

        private var database: StoryDatabase?= null
        var dao: StoryDAO?= null

        private var commentViewModel: CommentViewModel?= null
        private var application: Application?= null

        fun getInstance(application: Application): CommentViewModel {

            if (commentViewModel == null){
                commentViewModel = CommentViewModel()
                this.application = application
                database = StoryDatabase.getInstance(application)
                dao = database!!.getStoryDAO()
            }
            return commentViewModel!!
        }
    }

    fun initCommentRepo(disposable: CompositeDisposable, kids: ArrayList<Int>, id: Int, context: Context){
        commentRepo = CommentRepo(disposable, this, commentList, isCommentLoading)
        commentRepo!!.checkAndDownloadComments(kids, id, context)
    }

    fun isCommentLoading(): LiveData<Boolean> {
        return isCommentLoading
    }

    /*fun isReplyLoading(): LiveData<Boolean> {
        return isReplyLoading
    }*/

    fun isNetAvailable(): LiveData<Boolean> {
        return isNetAvailable
    }

    fun getCommentData(): LiveData<List<CommentModel>> {
        return commentList
    }

    /*fun getReplyData(): LiveData<List<CommentModel>> {
        return replyListList
    }*/
}