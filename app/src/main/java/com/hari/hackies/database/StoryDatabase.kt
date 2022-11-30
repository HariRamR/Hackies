package com.hari.hackies.database

import android.app.Application
import androidx.room.*
import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.StoryModel

@Database(entities = [StoryModel::class, CommentModel::class], version = 1)
@TypeConverters(TypeConverterDB::class)
abstract class StoryDatabase: RoomDatabase() {

    abstract fun getStoryDAO():StoryDAO

    companion object{
        private var database: StoryDatabase?=null

        fun getInstance(application: Application): StoryDatabase{

            if (database == null){
                database = Room.databaseBuilder(application.applicationContext, StoryDatabase::class.java,
                    "story_db").fallbackToDestructiveMigration().build()
            }
            return database!!
        }
    }
}