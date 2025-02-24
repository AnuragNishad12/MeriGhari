package com.example.merighari.Pages

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.merighari.Adapter.AlarmAdapter
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

class SetAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetAlarmBinding
    private lateinit var alarmAdapter: AlarmAdapter
    private val alarmList = mutableListOf<AlarmModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Initialize RecyclerView and Adapter
        alarmAdapter = AlarmAdapter(alarmList)
        binding.recyclerView.adapter = alarmAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Load existing alarms from Room database
        loadAlarms()

        // Set click listener to show time picker
        binding.btnShowPicker.setOnClickListener {
            showTimePickerDialog(this) { hour, minute, amPm ->
                saveAlarmToDatabase(hour, minute, amPm)
            }
        }
    }

    private fun showTimePickerDialog(context: Context, onTimeSelected: (Int, Int, String) -> Unit) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.dialog_time_picker)

        val numberPickerHour: NumberPicker = dialog.findViewById(R.id.numberPickerHour)!!
        val numberPickerMinute: NumberPicker = dialog.findViewById(R.id.numberPickerMinute)!!
        val numberPickerAmPm: NumberPicker = dialog.findViewById(R.id.numberPickerAmPm)!!
        val btnSetTime: Button = dialog.findViewById(R.id.btnSetTime)!!

        // Set ranges for pickers
        numberPickerHour.minValue = 1
        numberPickerHour.maxValue = 12

        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59

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
        val db = AppDatabase.getDatabase(this)
        val alarmDao = db.alarmDao()

        CoroutineScope(Dispatchers.IO).launch {
            val newAlarm = AlarmEntity(hour = hour, minute = minute, amPm = amPm)
            alarmDao.insertAlarm(newAlarm)
            loadAlarms()
        }
    }

    private fun loadAlarms() {
        val db = AppDatabase.getDatabase(this)
        val alarmDao = db.alarmDao()

        CoroutineScope(Dispatchers.IO).launch {
            val alarms = alarmDao.getAllAlarms()
            withContext(Dispatchers.Main) {
                alarmAdapter.setAlarms(alarms.map { AlarmModel(it.hour, it.minute, it.amPm) })
            }
        }
    }
}

