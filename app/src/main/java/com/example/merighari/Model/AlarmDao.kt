package com.example.merighari.Model

import androidx.room.*

@Dao
interface AlarmDao {
    @Insert
    suspend fun insertAlarm(alarm: AlarmEntity)

    @Query("SELECT * FROM alarms")
    suspend fun getAllAlarms(): List<AlarmEntity>

    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity)
}
