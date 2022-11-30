package com.hari.hackies.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "storyMaster")
data class StoryModel (

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") var id: Int?= null,
    @SerializedName("deleted" ) var deleted : Boolean?= false,
    @SerializedName("dead") var dead: Boolean? = false,
    @SerializedName("by") var by: String?= null,
    @SerializedName("descendants") var descendants: Int?= null,
    @SerializedName("kids") var kids: ArrayList<Int> = arrayListOf(),
    @SerializedName("score") var score: Int?= null,
    @SerializedName("time") var time: Long?= null,
    @SerializedName("date") var date: String?= null,
    @SerializedName("title") var title: String?= null,
    @SerializedName("type") var type: String?= null,
    @SerializedName("url") var url: String?= null,
    @SerializedName("nameBGClr") var nameBGClr: Int?= null
)