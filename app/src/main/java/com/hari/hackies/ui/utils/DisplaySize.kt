package com.hari.hackies.ui.utils

import android.content.Context

object DisplaySize {

    fun getDisplaySize(context: Context, isHeight: Boolean, requiredSize: Int?= 0): Double{

        if (isHeight){

            val height = context.resources.displayMetrics.heightPixels
            if (requiredSize == 0){
                return height.toDouble()
            }else{
                return height/requiredSize!!.toDouble()
            }
        }else{

            val width = context.resources.displayMetrics.widthPixels
            if (requiredSize == 0){
                return width.toDouble()
            }else{
                return width/requiredSize!!.toDouble()
            }
        }
    }
}