package com.example.merighari.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.merighari.Model.AlarmModel
import com.example.merighari.R

class AlarmAdapter(private val alarmList: MutableList<AlarmModel>) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTime: TextView = itemView.findViewById(R.id.alarmTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.alarmTime.text = "${alarm.hour}:${String.format("%02d", alarm.minute)} ${alarm.amPm}"
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    // Add new alarms
    fun addAlarm(alarm: AlarmModel) {
        alarmList.add(alarm)
        notifyDataSetChanged()
    }

    // Update the entire list
    fun setAlarms(alarms: List<AlarmModel>) {
        alarmList.clear()
        alarmList.addAll(alarms)
        notifyDataSetChanged()
    }
}
