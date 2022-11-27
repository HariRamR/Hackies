package com.hari.hackies.api

import com.hari.hackies.model.StoryModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("{method}.json")
    fun getSourceData(@Path("method") method: String?): Call<ArrayList<Int>>

    @GET("item/{id}.json")
    fun getStoryData(@Path("id") id: String?): Call<StoryModel>?
}