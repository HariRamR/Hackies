package com.hari.hackies.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.ReplyModel
import com.hari.hackies.model.StoryModel

@Dao()
interface StoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStories(stories: List<StoryModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<CommentModel>): List<Long>
    @RawQuery
    fun customQuery(query: SupportSQLiteQuery):Long
    @Query("DELETE FROM storyMaster WHERE id NOT IN (SELECT id From storyMaster ORDER BY time DESC LIMIT 500)")
    fun deleteOldStories():Int
    @Query("DELETE FROM commentMaster WHERE parent NOT IN (SELECT id FROM storyMaster) AND parent NOT IN (SELECT id FROM commentMaster)")
    fun deleteOldComments():Int
    @Query("SELECT * FROM storyMaster ORDER BY time DESC")
    fun getAllStories(): List<StoryModel>
    @Query("SELECT * FROM commentMaster WHERE parent = :parentID ORDER BY time DESC")
    fun getCommentsByParentID(parentID: Int): List<CommentModel>
    @Query("SELECT id FROM storyMaster")
    fun getStoryIDs(): List<Int>
}