package com.ather.mytimer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.*

class DateTimeDialog(private val context: Context,
                     private var dateTime: String) {
    var date_time = ""
    var mYear = 0
    var mMonth = 0
    var mDay = 0

    var mHour = 0
    var mMinute = 0

     fun datePicker(textView: TextView) {
        // Get Current Date
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context,
            { view, year, monthOfYear, dayOfMonth ->
                date_time = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                //*************Call Time Picker Here ********************
                tiemPicker(textView)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

     fun tiemPicker(textView: TextView) {
        // Get Current Time
        val c = Calendar.getInstance()
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(context,
            { view, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                textView.text = "$date_time $hourOfDay:$minute"
                dateTime = "$hourOfDay:$minute"
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
    }
}