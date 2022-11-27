package com.hari.hackies.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.model.StoryModel

@Dao()
interface StoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: StoryModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<CommentModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplies(replies: List<ReplyModel>)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateStories(stories: List<StoryModel>)
    @RawQuery
    fun customQuery(query: SupportSQLiteQuery):Long
    @Query("SELECT * FROM storyMaster")
    fun getAllStories(): List<StoryModel>
    @Query("SELECT * FROM commentMaster")
    fun getAllComments(): List<CommentModel>
    @Query("SELECT * FROM replyMaster")
    fun getAllReplies(): List<ReplyModel>
    @Query("SELECT id FROM storyMaster")
    fun getStoryIDs(): List<Int>
}