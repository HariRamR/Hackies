package com.hari.hackies.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hari.hackies.database.StoryDAO
import com.hari.hackies.database.StoryDatabase
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.StoryModel
import com.hari.hackies.repository.CommentRepo
import io.reactivex.disposables.CompositeDisposable

class CommentViewModel: ViewModel() {

    var commentList: MutableLiveData<List<CommentModel>> = MutableLiveData<List<CommentModel>>()
    private var commentRepo: CommentRepo?= null
    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    var dao: StoryDAO?= null

    companion object{

        private var database: StoryDatabase?= null

        private var commentViewModel: CommentViewModel?= null
        private var application: Application?= null

        fun getInstance(application: Application): CommentViewModel {

            if (commentViewModel == null){
                commentViewModel = CommentViewModel()
                this.application = application
                database = StoryDatabase.getInstance(application)
            }
            return commentViewModel!!
        }
    }

    fun initCommentRepo(disposable: CompositeDisposable, storyModel: StoryModel){
        dao = database!!.getStoryDAO()
        commentRepo = CommentRepo(disposable, this)
        commentRepo!!.checkAndDownloadComments(storyModel.kids, storyModel.id!!)
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getCommentsData(): LiveData<List<CommentModel>> {
        return commentList
    }
}