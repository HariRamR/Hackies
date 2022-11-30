package com.hari.hackies.repository

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.hari.hackies.api.ApiClient
import com.hari.hackies.model.CommentModel
import com.hari.hackies.ui.utils.CheckInternet
import com.hari.hackies.viewmodel.CommentViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.*

class CommentRepo(private val disposable: CompositeDisposable, private val viewModel: CommentViewModel?= null,
                  private val commentList: MutableLiveData<List<CommentModel>>, private val isLoading: MutableLiveData<Boolean>) {

    fun checkAndDownloadComments(commentIDs: ArrayList<Int>, parentId: Int, context: Context){

        isLoading.value = true
        if (!CheckInternet.isInternetAvailable(context)){
            doResultAction(parentId)
        }else {
            runBlocking {

                val localComments = getCommentsByParentID(parentId)
                if (localComments.size == commentIDs.size){

                    commentList.value = localComments
                    isLoading.value = false
                }
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
                    commentList.add(comment)
                }
            }
            runBlocking {

                insertComments(commentList)
            }
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
            val comments = getCommentsByParentID(parentId)
            if (comments.isEmpty()){
                viewModel!!.isNetAvailable.value = false
                commentList.value = listOf()
            }else
                commentList.value = comments
        }
        isLoading.value = false
    }

    private fun callComment(id: Int): Observable<Response<CommentModel>> {
        return ApiClient.getClient()!!.getCommentData(id.toString())
    }

    private suspend fun insertComments(comments: ArrayList<CommentModel>){

        val differ = ArrayList<Deferred<List<Long>>>()
        differ.add(
            GlobalScope.async {
                CommentViewModel.dao!!.insertComments(comments)
            }
        )
        differ.awaitAll()
    }

    private suspend fun getCommentsByParentID(parentId: Int): List<CommentModel>{

        val differ = ArrayList<Deferred<List<CommentModel>>>()
        differ.add(
            GlobalScope.async {
                CommentViewModel.dao!!.getCommentsByParentID(parentId)
            }
        )
        return differ.awaitAll()[0]
    }
}