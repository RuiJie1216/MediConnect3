package com.example.mylastapp.ui.rooms.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mylastapp.ui.rooms.entity.MedicalReminder
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: MedicalReminder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<MedicalReminder>)

    @Update
    suspend fun updateReminder(reminder: MedicalReminder)

    @Delete
    suspend fun deleteReminder(reminder: MedicalReminder)

    @Query("SELECT * FROM medical_reminder")
    fun getAllReminders(): Flow<List<MedicalReminder>>

    @Query("DELETE FROM medical_reminder")
    suspend fun deleteAllReminders()

    @Transaction
    suspend fun replaceAllReminders(reminders: List<MedicalReminder>) {
        deleteAllReminders()
        insertReminders(reminders)
    }


    @Query("SELECT * FROM medical_reminder WHERE user_ic = :userIc")
    fun getRemindersByIc(userIc: String): Flow<List<MedicalReminder>>


    @Query("SELECT * FROM medical_reminder WHERE id = :reminderId")
    suspend fun getReminderById(reminderId: Int): MedicalReminder?

}