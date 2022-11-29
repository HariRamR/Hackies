package com.hari.hackies.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
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
    var isNoData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    companion object{

        private var database: StoryDatabase?= null
        var dao: StoryDAO?= null

        private var storyViewModel: StoryViewModel?= null
        private var application: Application?= null

        fun getInstance(application: Application): StoryViewModel {

            if (storyViewModel == null){
                storyViewModel = StoryViewModel()
                this.application = application
                database = StoryDatabase.getInstance(application)
                dao = database!!.getStoryDAO()
            }
            return storyViewModel!!
        }
    }

    fun initStoryRepo(activity: Activity, disposable: CompositeDisposable, moveToDashboard: Boolean, context: Context){
        storyRepo = StoryRepo(disposable, storyViewModel!!, activity, moveToDashboard)
        storyRepo!!.getSourceFromAPI(context)
    }

    fun isLoading(): LiveData<Boolean>{
        return isLoading
    }

    fun isNoData(): LiveData<Boolean>{
        return isNoData
    }

    fun getStoriesData(): LiveData<List<StoryModel>>{
        return storyList
    }
}