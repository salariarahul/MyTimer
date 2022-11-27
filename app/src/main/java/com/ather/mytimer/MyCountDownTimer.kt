package com.ather.mytimer

import android.os.CountDownTimer
import com.ather.mytimer.databinding.ActivityMainBinding

class MyCountDownTimer(val binding: ActivityMainBinding) {
    lateinit var countdown_timer: CountDownTimer
    var isRunning: Boolean = false;
    var time_in_milli_seconds = 0L

     fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds,
            1000) {
            override fun onFinish() {
                binding.tvStatus.setText("done!")
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

        isRunning = true
        binding.tvStatus.text = "Pause"
    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60
        binding.tvTimer.text = "$minute:$seconds"
        binding.progressBarCircle.max = (time_in_milli_seconds / 1000).toInt()

        binding.progressBarCircle.progress  = (time_in_milli_seconds / 1000).toInt()
    }
}