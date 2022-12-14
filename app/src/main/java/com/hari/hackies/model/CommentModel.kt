package com.hari.hackies.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "commentMaster")
data class CommentModel (

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") var id: Int?= null,
    @SerializedName("deleted") var deleted: Boolean?= false,
    @SerializedName("dead") var dead: Boolean? = false,
    @SerializedName("by") var by: String?= null,
    @SerializedName("kids") var kids: ArrayList<Int> = arrayListOf(),
    @SerializedName("parent") var parent: Int?= null,
    @SerializedName("text") var text: String?= null,
    @SerializedName("time") var time: Long?= null,
    @SerializedName("date") var date: String?= null,
    @SerializedName("type") var type: String?= null,
    @SerializedName("nameBGClr") var nameBGClr: Int?= null

)