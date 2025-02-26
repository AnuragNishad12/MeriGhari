package com.example.merighari.Pages

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.merighari.Adapter.AlarmAdapter
import com.example.merighari.Model.AlarmDao
import com.example.merighari.Model.AlarmEntity
import com.example.merighari.Model.AlarmModel
import com.example.merighari.Model.AppDatabase
import com.example.merighari.R
import com.example.merighari.databinding.ActivitySetAlarmBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SetAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetAlarmBinding
    private lateinit var alarmDao: AlarmDao
    private lateinit var countdownTimer: CountDownTimer
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val db = AppDatabase.getDatabase(this)
        alarmDao = db.alarmDao()

        binding.btnShowPicker.setOnClickListener {
            showTimePickerDialog(this) { hour, minute, amPm ->
                saveAlarmToDatabase(hour, minute, amPm)
            }
        }

        // Load the latest alarm when activity starts
        loadLatestAlarm()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTimerRunning) {
            countdownTimer.cancel()
        }
    }

    private fun showTimePickerDialog(context: Context, onTimeSelected: (Int, Int, String) -> Unit) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.dialog_time_picker)

        val numberPickerHour: NumberPicker = dialog.findViewById(R.id.numberPickerHour)!!
        val numberPickerMinute: NumberPicker = dialog.findViewById(R.id.numberPickerMinute)!!
        val numberPickerAmPm: NumberPicker = dialog.findViewById(R.id.numberPickerAmPm)!!
        val btnSetTime: Button = dialog.findViewById(R.id.btnSetTime)!!

        numberPickerHour.minValue = 1
        numberPickerHour.maxValue = 12
        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59

        // Set up AM/PM picker - this is what was missing
        numberPickerAmPm.minValue = 0
        numberPickerAmPm.maxValue = 1
        numberPickerAmPm.displayedValues = arrayOf("AM", "PM")

        btnSetTime.setOnClickListener {
            val selectedHour = numberPickerHour.value
            val selectedMinute = numberPickerMinute.value
            val selectedAmPm = if (numberPickerAmPm.value == 0) "AM" else "PM"

            onTimeSelected(selectedHour, selectedMinute, selectedAmPm)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveAlarmToDatabase(hour: Int, minute: Int, amPm: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val newAlarm = AlarmEntity(hour = hour, minute = minute, amPm = amPm)
            alarmDao.insertAlarm(newAlarm)

            withContext(Dispatchers.Main) {
                loadLatestAlarm()
            }
        }
    }

    private fun loadLatestAlarm() {
        CoroutineScope(Dispatchers.IO).launch {
            val latestAlarm = alarmDao.getLatestAlarm()

            withContext(Dispatchers.Main) {
                if (latestAlarm != null) {
                    displayAlarm(latestAlarm)
                    calculateAlarmTime(latestAlarm)
                } else {
                    binding.tvNoAlarm.visibility = View.VISIBLE
                    binding.cardAlarm.visibility = View.GONE
                    binding.tvTimeUntilAlarm.visibility = View.GONE

                    // Cancel existing timer if any
                    if (isTimerRunning) {
                        countdownTimer.cancel()
                        isTimerRunning = false
                    }
                }
            }
        }
    }

    private fun displayAlarm(alarm: AlarmEntity) {
        // Make the alarm card visible
        binding.tvNoAlarm.visibility = View.GONE
        binding.cardAlarm.visibility = View.VISIBLE
        binding.tvTimeUntilAlarm.visibility = View.VISIBLE

        // Format the time with leading zeros for minutes
        val minuteStr = if (alarm.minute < 10) "0${alarm.minute}" else "${alarm.minute}"

        // Display the alarm time
        binding.tvAlarmTime.text = "${alarm.hour}:${minuteStr} ${alarm.amPm}"

        // Optional: Set up delete button
        binding.btnDeleteAlarm.setOnClickListener {
            deleteAlarm(alarm)
        }
    }

    private fun calculateAlarmTime(alarm: AlarmEntity) {
        // Calculate time until alarm
        val timeUntilAlarmMillis = calculateTimeUntilAlarm(alarm.hour, alarm.minute, alarm.amPm)

        // Convert to hours and minutes
        val hours = timeUntilAlarmMillis / (1000 * 60 * 60)
        val minutes = (timeUntilAlarmMillis % (1000 * 60 * 60)) / (1000 * 60)

        // Display time until alarm
        val timeText = when {
            hours > 0 -> {
                if (minutes > 0) {
                    "Alarm rings in $hours hours and $minutes minutes"
                } else {
                    "Alarm rings in $hours hours"
                }
            }
            minutes > 0 -> "Alarm rings in $minutes minutes"
            else -> "Alarm rings in less than a minute"
        }

        binding.tvTimeUntilAlarm.text = timeText

        // Optional: Start a periodic update
        startPeriodicUpdate(alarm)
    }

    private fun startPeriodicUpdate(alarm: AlarmEntity) {
        // Cancel existing timer if any
        if (isTimerRunning) {
            countdownTimer.cancel()
        }

        // Update every minute
        countdownTimer = object : CountDownTimer(Long.MAX_VALUE, 60000) {
            override fun onTick(millisUntilFinished: Long) {
                calculateAlarmTime(alarm)
            }

            override fun onFinish() {
                isTimerRunning = false
            }
        }.start()

        isTimerRunning = true
    }

    private fun calculateTimeUntilAlarm(hour: Int, minute: Int, amPm: String): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        // Create a calendar for the alarm time
        val alarmCalendar = Calendar.getInstance()

        // Set the alarm hour (convert to 24 hour format if needed)
        var alarmHour = hour
        if (amPm == "PM" && hour < 12) {
            alarmHour += 12
        } else if (amPm == "AM" && hour == 12) {
            alarmHour = 0
        }

        alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmHour)
        alarmCalendar.set(Calendar.MINUTE, minute)
        alarmCalendar.set(Calendar.SECOND, 0)
        alarmCalendar.set(Calendar.MILLISECOND, 0)

        // If alarm time is earlier than current time, set it for the next day
        if (alarmCalendar.timeInMillis <= now) {
            alarmCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return alarmCalendar.timeInMillis - now
    }

    private fun deleteAlarm(alarm: AlarmEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            alarmDao.deleteAlarm(alarm)

            withContext(Dispatchers.Main) {
                val anyAlarmsLeft = alarmDao.getAlarmCount() > 0
                if (!anyAlarmsLeft) {
                    binding.tvNoAlarm.visibility = View.VISIBLE
                    binding.cardAlarm.visibility = View.GONE
                    binding.tvTimeUntilAlarm.visibility = View.GONE

                    // Cancel the timer
                    if (isTimerRunning) {
                        countdownTimer.cancel()
                        isTimerRunning = false
                    }
                } else {
                    loadLatestAlarm()
                }
            }
        }
    }
}

