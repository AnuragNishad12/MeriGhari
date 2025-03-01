package com.example.merighari.Model

import androidx.room.*

@Dao
interface AlarmDao {
    @Insert
    suspend fun insertAlarm(alarm: AlarmModel)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmModel)

    @Query("SELECT * FROM alarms ORDER BY id DESC LIMIT 1")
    suspend fun getLatestAlarm(): AlarmModel?

    @Query("SELECT COUNT(*) FROM alarms")
    suspend fun getAlarmCount(): Int
}
