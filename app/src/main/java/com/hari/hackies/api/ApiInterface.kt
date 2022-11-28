package com.hari.hackies.api

import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.model.StoryModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("{method}.json")
    fun getSourceData(@Path("method") method: String?): Observable<Response<ArrayList<Int>>>

    @GET("item/{id}.json")
    fun getStoryData(@Path("id") id: String?): Observable<Response<StoryModel>>

    @GET("item/{id}.json")
    fun getCommentData(@Path("id") id: String?): Observable<Response<CommentModel>>

    @GET("item/{id}.json")
    fun getReplyData(@Path("id") id: String?): Observable<Response<ReplyModel>>
}