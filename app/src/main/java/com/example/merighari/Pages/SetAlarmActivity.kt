package com.example.merighari.Pages

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.merighari.AlarmActivity.AlarmReceiver
import com.example.merighari.AlarmActivity.AlarmsActivity
import com.example.merighari.Model.Alarm
import com.example.merighari.Model.AlarmDao
import com.example.merighari.Model.AlarmDatabase
import com.example.merighari.Model.AlarmModel
import com.example.merighari.R
import com.example.merighari.databinding.ActivitySetAlarmBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SetAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetAlarmBinding
    private lateinit var alarmDao: AlarmDao
    private lateinit var countdownTimer: CountDownTimer
    private var isTimerRunning = false
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        createNotificationChannel()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )




        val db = AlarmDatabase.getDatabase(this)
        alarmDao = db.alarmDao()

        binding.btnShowPicker.setOnClickListener {
            showTimePickerDialog(this) { hour, minute, amPm ->
                saveAlarmToDatabase(hour, minute, amPm)
            }
        }

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
        val tvAlarmDay: TextView = dialog.findViewById(R.id.tvAlarmDay) ?: TextView(context) // Fallback if not in layout

        numberPickerHour.minValue = 1
        numberPickerHour.maxValue = 12
        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59

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
            val newAlarm = AlarmModel(hour = hour, minute = minute, amPm = amPm)
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

                    if (isTimerRunning) {
                        countdownTimer.cancel()
                        isTimerRunning = false
                    }
                }
            }
        }
    }

//    private fun displayAlarm(alarm: AlarmModel) {
//        binding.tvNoAlarm.visibility = View.GONE
//        binding.cardAlarm.visibility = View.VISIBLE
//        binding.tvTimeUntilAlarm.visibility = View.VISIBLE
//
//        val minuteStr = if (alarm.minute < 10) "0${alarm.minute}" else "${alarm.minute}"
//        binding.tvAlarmTime.text = "${alarm.hour}:${minuteStr} ${alarm.amPm}"
//
//        binding.btnDeleteAlarm.setOnClickListener {
//            deleteAlarm(alarm)
//        }
//    }

    private fun displayAlarm(alarm: AlarmModel) {
        binding.tvNoAlarm.visibility = View.GONE
        binding.cardAlarm.visibility = View.VISIBLE
        binding.tvTimeUntilAlarm.visibility = View.VISIBLE

        val minuteStr = String.format("%02d", alarm.minute)
        binding.tvAlarmTime.text = "${alarm.hour}:$minuteStr ${alarm.amPm}"

        setAlarm(alarm) // Call setAlarm with the alarm data

        binding.btnDeleteAlarm.setOnClickListener {
            deleteAlarm(alarm)
        }
    }


    private fun calculateAlarmTime(alarm: AlarmModel) {
        val timeUntilAlarmMillis = calculateTimeUntilAlarm(alarm.hour, alarm.minute, alarm.amPm)
        val hours = timeUntilAlarmMillis / (1000 * 60 * 60)
        val minutes = (timeUntilAlarmMillis % (1000 * 60 * 60)) / (1000 * 60)

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
        startPeriodicUpdate(alarm)
    }

    private fun startPeriodicUpdate(alarm: AlarmModel) {
        if (isTimerRunning) {
            countdownTimer.cancel()
        }

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

        val alarmCalendar = Calendar.getInstance()

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

        if (alarmCalendar.timeInMillis <= now) {
            alarmCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return alarmCalendar.timeInMillis - now
    }

    private fun deleteAlarm(alarm: AlarmModel) {
        CoroutineScope(Dispatchers.IO).launch {
            alarmDao.deleteAlarm(alarm)

            withContext(Dispatchers.Main) {
                val anyAlarmsLeft = alarmDao.getAlarmCount() > 0
                if (!anyAlarmsLeft) {
                    binding.tvNoAlarm.visibility = View.VISIBLE
                    binding.cardAlarm.visibility = View.GONE
                    binding.tvTimeUntilAlarm.visibility = View.GONE

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

    private fun setAlarm(alarm: AlarmModel) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val timeInMillis = calendar.timeInMillis
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

        val minuteStr = String.format("%02d", alarm.minute)
        binding.tvAlarmTime.text = "${alarm.hour}:$minuteStr ${alarm.amPm}"
        Toast.makeText(this, "Alarm set for ${alarm.hour}:$minuteStr ${alarm.amPm}", Toast.LENGTH_SHORT).show()
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Channel for alarm notifications"
            channel.enableLights(true)
            channel.enableVibration(true)

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                SetAlarmActivity.NOTIFICATION_PERMISSION_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SetAlarmActivity.NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications won't work without permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 123
    }

}

