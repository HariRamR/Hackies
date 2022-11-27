package com.hari.hackies.model

import com.google.gson.annotations.SerializedName

data class StoryModel (

    @SerializedName("by") var by: String?= null,
    @SerializedName("descendants") var descendants: Int?= null,
    @SerializedName("id") var id: Int?= null,
    @SerializedName("kids") var kids: ArrayList<Int> = arrayListOf(),
    @SerializedName("score") var score: Int?= null,
    @SerializedName("time") var time: Long?= null,
    @SerializedName("time") var date: String?= null,
    @SerializedName("title") var title: String?= null,
    @SerializedName("type") var type: String?= null,
    @SerializedName("url") var url: String?= null,
    @SerializedName("isSelected") var isSelected: Boolean?= false

)