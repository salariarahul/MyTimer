package com.ather.mytimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.ather.mytimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initViews()
        checkTheme()
    }

    private fun initViews() {
        //Set Current Date.
        binding.tvDate.text = Utilis.getCurrentDate()
        binding.switchOnOff.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                MyPreference(this).darkMode = 1
                delegate.applyDayNight()
                // Change the app background color
               // root_layout.setBackgroundColor(Color.GREEN)
            } else {
                // The switch is disabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                MyPreference(this).darkMode = 0
                delegate.applyDayNight()

                // Set the app background color to light gray
                //root_layout.setBackgroundColor(Color.LTGRAY)

        } }
    }

    private fun checkTheme() {
        when (MyPreference(this).darkMode) {
            0 -> {
                binding.switchOnOff.isChecked = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                binding.switchOnOff.isChecked = true

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                delegate.applyDayNight()
            }
        }
    }
}