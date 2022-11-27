package com.ather.mytimer

import android.os.Bundle
import android.os.CountDownTimer
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
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
   private var mStartTime: String=""
    private var mEndTime: String=""

    var START_MILLI_SECONDS = 60000L

    lateinit var countdown_timer: CountDownTimer
    var isRunning: Boolean = false;
    var time_in_milli_seconds = 0L

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

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
            } else {
                // The switch is disabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                MyPreference(this).darkMode = 0
                delegate.applyDayNight()
        } }
    }

    override fun onResume() {
        super.onResume()
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
            DateTimeDialog(this, mStartTime).datePicker(dialogBinding?.tvStartTimeData!!)
        }

        dialogBinding?.tvEndTime?.setOnClickListener {
            DateTimeDialog(this, mEndTime).datePicker(dialogBinding?.tvEndTimeData!!)
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
            if(TextUtils.isEmpty(dialogBinding.tvEndTimeData.text) ||
                dialogBinding.tvEndTimeData.text == "0"){
                Toast.makeText(this, "Please select End time.",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            finalModal = TaskModal(dialogBinding.editTaskName.text.toString().trim(),
            mStartTime, mEndTime)

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
        val list = MyPreference(this).getList()
        if (!list.isNullOrEmpty()) {
            binding.tvTaskName.text = list.get(0).taskName
            binding.tvStartEndTime.text = list.get(0).startTime + "-" +list.get(0).endTime

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

 /*   private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                textView.setText("done!")
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

        isRunning = true
        button.text = "Pause"
        reset.visibility = View.INVISIBLE

    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

        timer.text = "$minute:$seconds"
    }*/

}