package com.hari.hackies.viewmodel

import android.app.Application
import android.graphics.Color
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.hari.hackies.api.ApiClient
import com.hari.hackies.api.ApiInterface
import com.hari.hackies.database.StoryDAO
import com.hari.hackies.database.StoryDatabase
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.utils.ConvertTime
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*
import kotlin.collections.ArrayList

class StoriesViewModel(): ViewModel() {

    private var storyList: MutableLiveData<List<StoryModel>>? = MutableLiveData<List<StoryModel>>()
    private var database: StoryDatabase?= null
    private var dao: StoryDAO?= null
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    private var isError: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private var application: Application?= null

    constructor(application: Application) : this() {

        if (database == null){
            this.application = application
            database = StoryDatabase.getInstance(application)
            dao = database!!.getStoryDAO()
//            runBlocking {
//
//                storyList!!.value = getAllStories()
//            }
//            isLoading.value = false
            getSourceFromAPI()
        }
    }

    private fun getSourceFromAPI(){

        val apiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)
        val call = apiInterface.getSourceData("newstories")
        call.enqueue(object: Callback<ArrayList<Int>>{

            override fun onResponse(call: Call<ArrayList<Int>>, response: Response<ArrayList<Int>>) {

                if (response.isSuccessful){

                    val storyIDs = response.body()

                    runBlocking {

                        val localStoryIDs = getStoryIDs()
                        for (id in storyIDs!!){

                            if (!localStoryIDs.contains(id)){

                                getStoryFromAPI(id.toString())
                            }
                        }
                        deleteOldData()
                        storyList!!.value = getAllStories()
                        isLoading.value = false
                    }
                }else {

                    isError.value = true
                    isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<Int>>, t: Throwable) {

                isError.value = true
                isLoading.value = false
                Log.e("getSourceFromAPI ", t.message.toString())
            }
        })
    }

    fun getStoryFromAPI(id: String){

        val apiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)
        val call = apiInterface.getStoryData(id)
        call!!.enqueue(object: Callback<StoryModel>{
            override fun onResponse(call: Call<StoryModel>, response: Response<StoryModel>) {

                if(response.isSuccessful){

                    val story = response.body()
                    val random = Random()
                    val nameBgClr = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
                    story!!.nameBGClr = nameBgClr
                    runBlocking {
                        insertStories(story)
                    }
                }
            }

            override fun onFailure(call: Call<StoryModel>, t: Throwable) {
                Log.e("getStoryFromAPI Error ", t.message.toString())
            }
        })
    }

    fun insertStories(story: StoryModel){

        GlobalScope.async {
            dao!!.insertStory(story)
        }
    }

    fun deleteOldData(){
        GlobalScope.async {
            dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM storyMaster WHERE id NOT IN (SELECT id From storyMaster ORDER BY time DESC LIMIT 500)"))
            dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM commentMaster WHERE id NOT IN (SELECT id FROM storyMaster)"))
            dao!!.customQuery(SimpleSQLiteQuery("DELETE FROM replyMaster WHERE id NOT IN (SELECT id FROM storyMaster)"))
        }
    }

    fun insertComments(comments: List<CommentModel>){

        GlobalScope.async {
            dao!!.insertComments(comments)
        }
    }

    fun insertReplies(replies: List<ReplyModel>){

        GlobalScope.async {
            dao!!.insertReplies(replies)
        }
    }

    private suspend fun getAllStories(): List<StoryModel>{

        val differ = ArrayList<Deferred<List<StoryModel>>>()
        differ.add(
            GlobalScope.async {
                dao!!.getAllStories()
            }
        )
        return differ.awaitAll()[0]
    }

    private suspend fun getStoryIDs(): List<Int>{

        val differ = ArrayList<Deferred<List<Int>>>()
        differ.add(
            GlobalScope.async {
                dao!!.getStoryIDs()
            }
        )
        return differ.awaitAll()[0]
    }

    fun isLoading(): LiveData<Boolean>{
        return isLoading
    }

    fun isError(): LiveData<Boolean>{
        return isError
    }

    fun getStoriesData(): LiveData<List<StoryModel>>{
        return storyList!!
    }
}