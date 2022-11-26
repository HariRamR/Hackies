package com.hari.hackies.ui.utils

import java.util.concurrent.TimeUnit

internal class ConvertTime {

    companion object{

        fun format(timestamp: Long): String {

            val millisFromNow = getMillisFromNow(timestamp)
            val minutesFromNow = TimeUnit.MILLISECONDS.toMinutes(millisFromNow)
            if (minutesFromNow < 1) {
                return "just now"
            }
            val hoursFromNow = TimeUnit.MILLISECONDS.toHours(millisFromNow)
            if (hoursFromNow < 1) {
                return formatMinutes(minutesFromNow)
            }
            val daysFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow)
            if (daysFromNow < 1) {
                return formatHours(hoursFromNow)
            }
            val weeksFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow) / 7
            if (weeksFromNow < 1) {
                return formatDays(daysFromNow)
            }
            val monthsFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow) / 30
            if (monthsFromNow < 1) {
                return formatWeeks(weeksFromNow)
            }
            val yearsFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow) / 365
            return if (yearsFromNow < 1) {
                formatMonths(monthsFromNow)
            } else formatYears(yearsFromNow)
        }

        private fun getMillisFromNow(commentedAt: Long): Long {
//        val commentedAtMillis = commentedAt.time
            val nowMillis = System.currentTimeMillis()
            return nowMillis - commentedAt
        }

        private fun formatMinutes(minutes: Long): String {
            return format(minutes, " minute ago", " minutes ago")
        }

        private fun formatHours(hours: Long): String {
            return format(hours, " hour ago", " hours ago")
        }

        private fun formatDays(days: Long): String {
            return format(days, " day ago", " days ago")
        }

        private fun formatWeeks(weeks: Long): String {
            return format(weeks, " week ago", " weeks ago")
        }

        private fun formatMonths(months: Long): String {
            return format(months, " month ago", " months ago")
        }

        private fun formatYears(years: Long): String {
            return format(years, " year ago", " years ago")
        }

        private fun format(hand: Long, singular: String, plural: String): String {
            return if (hand == 1L) {
                hand.toString() + singular
            } else {
                hand.toString() + plural
            }
        }
    }
}