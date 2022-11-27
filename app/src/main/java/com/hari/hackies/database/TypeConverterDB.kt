package com.hari.hackies.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverterDB {

    @TypeConverter
    fun restoreList(listOfString: String?): ArrayList<Int?>? {
        return Gson().fromJson(listOfString, object : TypeToken<ArrayList<Int?>?>() {}.type)
    }

    @TypeConverter
    fun saveList(listOfString: ArrayList<Int?>?): String? {
        return Gson().toJson(listOfString)
    }
}