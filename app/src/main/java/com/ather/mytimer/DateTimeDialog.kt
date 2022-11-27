package com.ather.mytimer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DateTimeDialog(private val context: Context) {
    var date_time = ""
    private var hour: Int = 0
    private var minute: Int = 0
    private val myCalendar = Calendar.getInstance()

    fun newDatePicker(textView: TextView, mStartTime: (time:String,miniseconds: Long) -> Unit){
        val datePickerDialog = DatePickerDialog(
            context, { view, year, monthOfYear, dayOfMonth ->
                date_time = Utilis.getFormattedDateInString(myCalendar.timeInMillis, "dd/MM/YYYY")

                //Call Time Picker Here
                newTimePicker(textView, mStartTime)
            }, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
        datePickerDialog.show()
    }

    private fun newTimePicker(textView: TextView,
                              mStartTime: (time: String,
                                           miniseconds: Long) -> Unit) {
        val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener(function = { _, hour, minute ->
                    myCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    myCalendar.set(Calendar.MINUTE, minute)
                    myCalendar.set(Calendar.SECOND, 0)

                    textView.text = "$date_time $hour:$minute"

                    val currentTime = Calendar.getInstance().time
                    val endDateDay = "$date_time $hour:$minute" /*"03/02/2020 21:00:00"*/
                    val format1 = SimpleDateFormat("dd/MM/yyyy hh:mm",
                        Locale.getDefault()
                    )
                    val endDate = format1.parse(endDateDay)

                    //milliseconds
                    val different = endDate.time - currentTime.time
                    mStartTime.invoke( "$hour:$minute", different)
                }), hour, minute,false
            )
        timePickerDialog.show()
    }
}