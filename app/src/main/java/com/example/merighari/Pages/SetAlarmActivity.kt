package com.example.merighari.Pages

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import com.example.merighari.R
import com.example.merighari.databinding.ActivitySetAlarmBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SetAlarmActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetAlarmBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val btnShowPicker: ImageView = findViewById(R.id.btnShowPicker)
        btnShowPicker.setOnClickListener {
            showTimePickerDialog(this) { hour, minute, amPm ->
                Toast.makeText(this, "Selected Time: $hour:$minute $amPm", Toast.LENGTH_SHORT).show()
            }
        }

//        val numberPickerHour: NumberPicker = findViewById(R.id.numberPickerHour)
//        val numberPickerMinute: NumberPicker = findViewById(R.id.numberPickerMinute)
//        val numberPickerAmPm: NumberPicker = findViewById(R.id.numberPickerAmPm)

//// Set values for Hour Picker (1-12)
//        numberPickerHour.minValue = 1
//        numberPickerHour.maxValue = 12
//        numberPickerHour.wrapSelectorWheel = false // Prevent looping
//
//// Set values for Minute Picker (0-59)
//        numberPickerMinute.minValue = 0
//        numberPickerMinute.maxValue = 59
//        numberPickerMinute.wrapSelectorWheel = false // Prevent looping
//
//// Set values for AM/PM Picker
//        numberPickerAmPm.minValue = 0
//        numberPickerAmPm.maxValue = 1
//        numberPickerAmPm.displayedValues = arrayOf("AM", "PM")
//        numberPickerAmPm.wrapSelectorWheel = false // Prevent looping



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
        numberPickerHour.wrapSelectorWheel = true

        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59
        numberPickerMinute.wrapSelectorWheel = true

        numberPickerAmPm.minValue = 0
        numberPickerAmPm.maxValue = 1
        numberPickerAmPm.displayedValues = arrayOf("AM", "PM")
        numberPickerAmPm.wrapSelectorWheel = true

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