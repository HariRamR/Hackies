package com.hari.hackies.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hari.hackies.database.StoryDAO
import com.hari.hackies.database.StoryDatabase
import com.hari.hackies.model.StoryModel
import com.hari.hackies.repository.StoryRepo
import io.reactivex.disposables.CompositeDisposable

class StoryViewModel: ViewModel() {

    var storyList: MutableLiveData<List<StoryModel>> = MutableLiveData<List<StoryModel>>()
    private var storyRepo: StoryRepo?= null
    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    var isError: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var dao: StoryDAO?= null

    companion object{

        private var database: StoryDatabase?= null

        private var storyViewModel: StoryViewModel?= null
        private var application: Application?= null

        fun getInstance(application: Application): StoryViewModel {

            if (storyViewModel == null){
                storyViewModel = StoryViewModel()
                this.application = application
                database = StoryDatabase.getInstance(application)
            }
            return storyViewModel!!
        }
    }

    fun initStoryRepo(activity: Activity, disposable: CompositeDisposable, moveToDashboard: Boolean){
        dao = database!!.getStoryDAO()
        storyRepo = StoryRepo(disposable, storyViewModel!!, activity, moveToDashboard)
        storyRepo!!.getSourceFromAPI()
    }

    fun isLoading(): LiveData<Boolean>{
        return isLoading
    }

    fun isError(): LiveData<Boolean>{
        return isError
    }

    fun getStoriesData(): LiveData<List<StoryModel>>{
        return storyList
    }
}