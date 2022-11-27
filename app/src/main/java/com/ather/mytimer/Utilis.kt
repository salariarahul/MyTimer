package com.ather.mytimer

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Utilis {
    companion object {
        fun getCurrentDate(): String {
             val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
        return formatter.format(currentTime)
        }

        fun getFormattedDateInString(timeInMillis: Long, format: String): String {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(timeInMillis)
        }

        fun getNextDifference(nextDiff: Long): String {
            return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(nextDiff),
                TimeUnit.MILLISECONDS.toMinutes(nextDiff) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(nextDiff)),
                TimeUnit.MILLISECONDS.toSeconds(nextDiff) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(nextDiff)))
        }
    }
}