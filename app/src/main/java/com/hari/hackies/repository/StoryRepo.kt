package com.hari.hackies.repository

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hari.hackies.api.ApiClient
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.Dashboard
import com.hari.hackies.viewmodel.StoryViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class StoryRepo(private val disposable: CompositeDisposable, private val viewModel: StoryViewModel, private val activity: Activity, private val moveToDashboard: Boolean=false) {

    fun getSourceFromAPI(){

        disposable.add(
            ApiClient.getClient()!!.getSourceData("newstories")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { ids ->
                        runBlocking {

                            val storyIDs = ids.body()
                            val localStoryIDs = getStoryIDs()
                            val fetchList = arrayListOf<Observable<Response<StoryModel>>>()
                            for (id in storyIDs!!){

                                if (!localStoryIDs.contains(id)){

                                    fetchList.add(callStory(id))
                                }
                            }
                            if (fetchList.size == 0) doResultAction()
                            else disposable.add(getStoriesFromAPI(fetchList))
                        }
                    },
                    { throwable ->
                        Log.e("getSourceFromAPI ", throwable.message ?: "onError")
                        viewModel.isError.value = false
                        viewModel.isLoading.value = false
                    }
                )
        )
    }

    private fun getStoriesFromAPI(observableList: ArrayList<Observable<Response<StoryModel>>>): Disposable {

        return Observable.zip(observableList){

            val storyList = ArrayList<StoryModel>()
            it.forEach{
                    storyModelRes ->
                val res: Response<StoryModel> = storyModelRes as Response<StoryModel>
                if (res.code() == 200){

                    val story = res.body()
                    val random = Random()
                    val nameBgClr = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
                    story!!.nameBGClr = nameBgClr
                    storyList.add(story)
//                    insertStories(story)
                }
            }
            insertStories(storyList)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                doResultAction()
            },
            {
                if (moveToDashboard){
                    activity.startActivity(Intent(activity, Dashboard::class.java))
                    activity.finish()
                }
            }
        )
    }

    private fun doResultAction(){
        runBlocking {
            val storyList = getAllStories()
            viewModel.storyList.value = storyList
        }
        viewModel.isLoading.value = false
        if (moveToDashboard){
            activity.startActivity(Intent(activity, Dashboard::class.java))
            activity.finish()
        }
    }

    private fun callStory(id: Int): Observable<Response<StoryModel>>{
        return ApiClient.getClient()!!.getStoryData(id.toString())
    }

    private fun insertStories(story: ArrayList<StoryModel>){

        GlobalScope.async {
            viewModel.dao!!.insertStory(story)
        }
    }

    fun deleteOldData(){
        GlobalScope.async {
            viewModel.dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM storyMaster WHERE id NOT IN (SELECT id From storyMaster ORDER BY time DESC LIMIT 500)"))
            viewModel.dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM commentMaster WHERE parent NOT IN (SELECT id FROM storyMaster)"))
            viewModel.dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM replyMaster WHERE parent NOT IN (SELECT id FROM commentMaster)"))
        }
    }

    fun insertReplies(replies: List<ReplyModel>){

        GlobalScope.async {
            viewModel.dao!!.insertReplies(replies)
        }
    }

    private suspend fun getAllStories(): List<StoryModel>{

        val differDelete = ArrayList<Deferred<Long>>()
        differDelete.add(GlobalScope.async {
            viewModel.dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM storyMaster WHERE id NOT IN (SELECT id From storyMaster ORDER BY time DESC LIMIT 500)"))
            viewModel.dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM commentMaster WHERE parent NOT IN (SELECT id FROM storyMaster)"))
            viewModel.dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM replyMaster WHERE parent NOT IN (SELECT id FROM commentMaster)"))
        })
        differDelete.awaitAll()

        val differ = ArrayList<Deferred<List<StoryModel>>>()
        differ.add(
            GlobalScope.async {
                viewModel.dao!!.getAllStories()
            }
        )
        return differ.awaitAll()[0]
    }

    private suspend fun getStoryIDs(): List<Int>{

        val differ = ArrayList<Deferred<List<Int>>>()
        differ.add(
            GlobalScope.async {
                viewModel.dao!!.getStoryIDs()
            }
        )
        return differ.awaitAll()[0]
    }
}