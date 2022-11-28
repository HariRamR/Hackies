package com.hari.hackies.repository

import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.hari.hackies.api.ApiClient
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.StoryModel
import com.hari.hackies.ui.Dashboard
import com.hari.hackies.viewmodel.CommentViewModel
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

class CommentRepo(private val disposable: CompositeDisposable, private val viewModel: CommentViewModel) {

    fun checkAndDownloadComments(commentIDs: ArrayList<Int>, parentId: Int){

        runBlocking {

            val localComments = getCommentsByParentID(parentId)
            if (localComments.size == commentIDs.size)
                viewModel.commentList.value = localComments
            else{

                val localCommentIDs = ArrayList<Int>()
                localComments.forEach {
                    comment -> localCommentIDs.add(comment.id!!)
                }
                val fetchList = arrayListOf<Observable<Response<CommentModel>>>()
                commentIDs.forEach {
                    id -> if (!localCommentIDs.contains(id)){
                        fetchList.add(callComment(id))
                    }
                }

                if (fetchList.size == 0) doResultAction(parentId)
                else disposable.add(getCommentsFromAPI(fetchList, parentId))
            }
        }
    }

    private fun getCommentsFromAPI(observableList: ArrayList<Observable<Response<CommentModel>>>, parentId: Int): Disposable {

        return Observable.zip(observableList){

            val commentList = ArrayList<CommentModel>()
            it.forEach{
                    storyModelRes ->
                val res: Response<CommentModel> = storyModelRes as Response<CommentModel>
                if (res.code() == 200){

                    val comment = res.body()
                    val random = Random()
                    val nameBgClr = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
                    comment!!.nameBGClr = nameBgClr
//                    insertComments(comment)
                    commentList.add(comment)
                }
            }
            insertComments(commentList)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                doResultAction(parentId)
            },
            {
                doResultAction(parentId)
            }
        )
    }

    private fun doResultAction(parentId: Int){
        runBlocking {
            val commentList = getCommentsByParentID(parentId)
            viewModel.commentList.value = commentList
        }
        viewModel.isLoading.value = false
    }

    private fun callComment(id: Int): Observable<Response<CommentModel>> {
        return ApiClient.getClient()!!.getCommentData(id.toString())
    }

    private fun insertComments(comments: ArrayList<CommentModel>){

        GlobalScope.async {
            viewModel.dao!!.insertComments(comments)
        }
    }

    private suspend fun getCommentsByParentID(parentId: Int): List<CommentModel>{

        val differ = ArrayList<Deferred<List<CommentModel>>>()
        differ.add(
            GlobalScope.async {
                viewModel.dao!!.getCommentsByParentID(parentId)
            }
        )
        return differ.awaitAll()[0]
    }
}