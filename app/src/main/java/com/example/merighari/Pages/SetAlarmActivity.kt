package com.example.merighari.Pages

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.merighari.Adapter.AlarmAdapter
import com.example.merighari.Model.AlarmModel
import com.example.merighari.R
import com.example.merighari.databinding.ActivitySetAlarmBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SetAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetAlarmBinding
    private lateinit var alarmAdapter: AlarmAdapter
    private val alarmList = mutableListOf<AlarmModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        alarmAdapter = AlarmAdapter(alarmList)
        binding.recyclerViewAlarms.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAlarms.adapter = alarmAdapter


        binding.btnShowPicker.setOnClickListener {
            showTimePickerDialog(this) { hour, minute, amPm ->
                val newAlarm = AlarmModel(hour, minute, amPm)
                alarmAdapter.addAlarm(newAlarm)
                Toast.makeText(this, "Alarm Set: $hour:$minute $amPm", Toast.LENGTH_SHORT).show()
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

        // Setup Number Pickers
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
}
