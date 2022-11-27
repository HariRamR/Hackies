package com.hari.hackies.model

import com.google.gson.annotations.SerializedName

data class ReplyModel (

    @SerializedName("by") var by: String?= null,
    @SerializedName("id") var id: Int?= null,
    @SerializedName("kids") var kids: ArrayList<Int> = arrayListOf(),
    @SerializedName("parent") var parent: Int?= null,
    @SerializedName("text") var text: String?= null,
    @SerializedName("time") var time: Long?= null,
    @SerializedName("time") var date: String?= null,
    @SerializedName("type") var type: String?= null

)