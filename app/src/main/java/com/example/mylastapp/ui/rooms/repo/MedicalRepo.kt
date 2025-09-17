package com.example.mylastapp.ui.rooms.repo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mylastapp.ui.rooms.dao.MedicalDao
import com.example.mylastapp.ui.rooms.entity.MedicalReminder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MedicalRepo(
    private val medicalDao: MedicalDao
) {
    private val db = FirebaseFirestore.getInstance()

    suspend fun insertReminder(reminder: MedicalReminder) {
        medicalDao.insertReminder(reminder)
    }

    suspend fun insertReminders(reminders: List<MedicalReminder>) {
        medicalDao.insertReminders(reminders)
    }

    suspend fun updateReminder(reminder: MedicalReminder) {
        medicalDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: MedicalReminder) {
        medicalDao.deleteReminder(reminder)
    }


    fun getRemindersByIc(userIc: String): Flow<List<MedicalReminder>> = medicalDao.getRemindersByIc(userIc)

    suspend fun syncFromFirebase(): Boolean = try {
        val snapshot = db.collection("medicalReminder").get().await()
        val reminders = snapshot.documents.map { doc ->
            MedicalReminder(
                id = doc.getLong("id")?.toInt() ?: 0,
                ic = doc.getString("ic") ?: "",
                name = doc.getString("medicalName") ?: "",
                dose = doc.getLong("dose")?.toInt() ?: 0,
                instruction = doc.getString("instruction") ?: "",
                date = doc.getString("date") ?: "",
                time = doc.getString("time") ?: ""
            )
        }

        Log.d("SyncDebug", "Reminders: $reminders")
        Log.d("SyncDebug", "Reminder inserted: ${reminders.size}")
        medicalDao.replaceAllReminders(reminders)
        Log.d("SyncDebug", "Reminders synced from Firebase successfully")

        true
    } catch (e: Exception) {
        Log.e("SyncDebug", "Error syncing from Firebase", e)
        false
    }



}