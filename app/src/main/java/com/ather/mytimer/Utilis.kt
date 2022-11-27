package com.ather.mytimer

import java.text.SimpleDateFormat
import java.util.*

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
    }
}