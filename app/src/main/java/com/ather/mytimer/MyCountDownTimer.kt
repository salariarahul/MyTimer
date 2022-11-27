package com.ather.mytimer

import android.content.Context
import android.os.CountDownTimer
import com.ather.mytimer.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MyCountDownTimer(val binding: ActivityMainBinding) {
    lateinit var countdown_timer: CountDownTimer
    var isRunning: Boolean = false;
    var time_in_milli_seconds = 0L

     fun startTimer(context: Context,
                    time_in_seconds: Long,
                    taskModal: TaskModal,
                    nextDiff: Long, callback: (isFinised: Boolean) -> Unit) {

         binding.progressBarCircle.max = (time_in_seconds / 1000).toInt()
         countdown_timer = object : CountDownTimer(
            time_in_seconds, 1000) {

            override fun onFinish() {
                isRunning = false
                binding.tvStatus.setText("done!")
                callback.invoke(isRunning)
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI(nextDiff)
            }
        }
        countdown_timer.start()

        isRunning = true
        binding.tvStatus.text = "${taskModal.taskName} task"
     }

    private fun updateTextUI(nextDiff: Long) {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

       binding.tvTimer.text = Utilis.getNextDifference(time_in_milli_seconds)

        val progress = (time_in_milli_seconds / 1000).toInt()
        binding.progressBarCircle.progress  = binding.progressBarCircle.max - progress
    }
}