package com.example.merighari.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.merighari.Model.AlarmModel
import com.example.merighari.R
import java.util.*

class AlarmAdapter(
    private val alarmList: MutableList<AlarmModel>,
    private val onDeleteClick: (AlarmModel) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTime: TextView = itemView.findViewById(R.id.alarmTime)
        val daytext: TextView = itemView.findViewById(R.id.textView4)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.alarmTime.text = "${alarm.hour}:${String.format("%02d", alarm.minute)} ${alarm.amPm}"

        val days = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val calendar = Calendar.getInstance()
        val today = days[calendar.get(Calendar.DAY_OF_WEEK) - 1]

        holder.daytext.text = today

        // Delete button click listener
        holder.deleteButton.setOnClickListener {
            onDeleteClick(alarm)
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    fun addAlarm(alarm: AlarmModel) {
        alarmList.add(alarm)
        notifyDataSetChanged()
    }

    fun setAlarms(alarms: List<AlarmModel>) {
        alarmList.clear()
        alarmList.addAll(alarms)
        notifyDataSetChanged()
    }

    fun removeAlarm(alarm: AlarmModel) {
        alarmList.remove(alarm)
        notifyDataSetChanged()
    }
}

