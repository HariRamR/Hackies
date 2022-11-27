package com.hari.hackies.ui.utils

object StringExtensions {

    fun String.capitalizeFirstLetter() = if (toCharArray().isEmpty()) "" else replaceFirst(toCharArray()[0], toCharArray()[0].uppercaseChar())
}