package com.ather.mytimer

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ather.mytimer.databinding.ActivityMainBinding
import com.ather.mytimer.databinding.DialogScheduleTaskBinding
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
   private var mStartTime: String=""
    private var mDate: Date? = null

    companion object{
        var isRunning = false
        var isDifference = false
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

        initViews()
        checkTheme()
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
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
            } else {
                // The switch is disabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                MyPreference(this).darkMode = 0
                delegate.applyDayNight()
        }
            isRunning = false
            isDifference = false
        }
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

    fun onClick(view: View){
        when(view.id){
            R.id.scheduleTask -> {
                showCustomDialog()
            }
            R.id.ivArrow -> {
                manageVisibility()
            }
        }
    }

    private fun manageVisibility() {
        if (binding.rvTasks.isVisible){
            //Gone Rv and visible view.
            binding.ivArrow.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_up))

            binding.rvTasks.visibility = View.GONE

            binding.ivIcon.visibility = View.VISIBLE
            binding.tvTaskName.visibility = View.VISIBLE
            binding.tvStartEndTime.visibility = View.VISIBLE

        }else{
            //Visible Rv and gone view.
            binding.ivArrow.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_down))
            binding.rvTasks.visibility = View.VISIBLE

            binding.ivIcon.visibility = View.GONE
            binding.tvTaskName.visibility = View.GONE
            binding.tvStartEndTime.visibility = View.GONE
        }
    }

    private fun showCustomDialog() {
        var finalModal : TaskModal? = null
        mStartTime = ""
        mDate = null
        val dialogBinding: DialogScheduleTaskBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_schedule_task,
                null,
                false
            )

        val customDialog = AlertDialog.Builder(this, 0).create()

        customDialog.apply {
            setView(dialogBinding?.root)
        }.show()

        dialogBinding?.tvStartTime?.setOnClickListener {
            DateTimeDialog(this).newDatePicker(dialogBinding?.tvStartTimeData!!) {
                    time, date ->
                mStartTime = time
                mDate = date
            }
        }

        dialogBinding?.btnCreate?.setOnClickListener {
            val modalNew = MyPreference(this).getList()

            if(TextUtils.isEmpty(dialogBinding.editTaskName.text)){
                Toast.makeText(this, "Please enter task name.",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(dialogBinding.tvStartTimeData.text) ||
                dialogBinding.tvStartTimeData.text == "0"){
                Toast.makeText(this, "Please select Start time.",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            finalModal = TaskModal(
                dialogBinding.editTaskName.text.toString().trim(),
                mStartTime, mDate!!
            )

            if (!modalNew.isNullOrEmpty()) {
                modalNew.add(finalModal!!)
                MyPreference(this).setList(modalNew)
            }else{
                val list = ArrayList<TaskModal>()
                list.add(finalModal!!)
                MyPreference(this).setList(list)
            }
            customDialog.dismiss()
            setAdapter()

            Toast.makeText(this, "Task Created", Toast.LENGTH_SHORT).show()
        }
    }

    fun setAdapter() {
        val list = MyPreference(this).getList().sortedBy { it.date.time }
        if (!list.isNullOrEmpty()) {
            list.forEachIndexed { index, it ->
                 //milliseconds Difference.
                val currentTime = Calendar.getInstance().time
                val difference = it.date.time - currentTime.time
                var nextDiff = 0L

                if (difference > 0 ) {
                    if (!isRunning){
                        isRunning = true
                        binding.tvTaskName.text = it.taskName
                        binding.tvStartEndTime.text = it.startTime
                        MyCountDownTimer(binding).startTimer(this, difference, it, nextDiff) {
                        isRunning = it
                        isDifference = false
                        setAdapter()
                    }
                    }
                    if (list.size > index+1){
                        nextDiff = list.get(index +1).date.time - it.date.time
                        //Next task time difference
                        if (nextDiff > 0 && !isDifference) {
                            isDifference = true
                            binding.tvRemainingTiming.text =
                                "Next task time \n difference \n ${Utilis.getNextDifference(nextDiff)}"
                        }
                    }
                    return@forEachIndexed
                }
            }

            val adapter = TaskAdapter(this, list) {
                //on Item click.
                Toast.makeText(this, "Task clicked", Toast.LENGTH_SHORT).show()
            }

            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.orientation = RecyclerView.VERTICAL
            binding.rvTasks.layoutManager = linearLayoutManager
            binding.rvTasks.addItemDecoration(DividerItemDecoration(this,
                linearLayoutManager.orientation))
            binding.rvTasks.adapter = adapter

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        isDifference = false
    }
}