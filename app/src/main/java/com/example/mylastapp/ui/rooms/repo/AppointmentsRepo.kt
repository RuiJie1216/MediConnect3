package com.example.mylastapp.ui.rooms.repo

import android.util.Log
import com.example.mylastapp.ui.rooms.dao.AppointmentsDao
import com.example.mylastapp.ui.rooms.entity.Appointments
import com.example.mylastapp.ui.rooms.entity.AppointmentsWithDoctorUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AppointmentsRepo(
    private val appointmentsDao: AppointmentsDao
) {
    private val db = FirebaseFirestore.getInstance()

    suspend fun insert(appointment: Appointments): Long = appointmentsDao.insertAppointment(appointment)

    suspend fun insertAll(appointments: List<Appointments>) = appointmentsDao.insertAppointments(appointments)

    suspend fun update(appointment: Appointments) = appointmentsDao.updateAppointment(appointment)

    suspend fun updateAll(appointments: List<Appointments>) = appointmentsDao.updateAppointments(appointments)

    suspend fun delete(appointment: Appointments) = appointmentsDao.deleteAppointment(appointment)

    suspend fun deleteAll() = appointmentsDao.deleteAllAppointments()

    fun getAllAppointments() = appointmentsDao.getAllAppointments()

    fun getAppointmentsByDoctorIDAndDate(doctorID: String, date: String): Flow<List<AppointmentsWithDoctorUser>> = appointmentsDao.getAppointmentsByDoctorIDAndDate(doctorID, date)

    fun getAppointmentsByUserIC(userIC: String): Flow<List<AppointmentsWithDoctorUser>> = appointmentsDao.getAppointmentsByUserIC(userIC)

    suspend fun getBookedTimes(doctorId: String, date: String): List<String> = appointmentsDao.getBookedTimes(doctorId, date)

    suspend fun syncFromFirebase(): Boolean = try {
        val snapshot = db.collection("appointments").get().await()
        val appointments = snapshot.documents.map { doc ->
            Appointments(
                appointmentID = doc.getLong("appointmentID")?.toInt() ?: 0,
                doctorID = doc.getString("doctorID") ?: "",
                userIC = doc.getString("userIC") ?: "",
                date = doc.getString("date") ?: "",
                time = doc.getString("time") ?: "",
                complaint = doc.getString("complaint") ?: "",
                status = doc.getString("status") ?: "",
            )
        }
        Log.d("SyncDebug", "Appointments: $appointments")
        Log.d("SyncDebug", "Appointment inserted: ${appointments.size}")
        appointmentsDao.replaceAllAppointments(appointments)
        Log.d("SyncDebug", "Appointments synced from Firebase successfully")

        true
    } catch (e: Exception) {
        Log.e("SyncDebug", "Error syncing from Firebase", e)
        false
    }


    suspend fun updateStatus(appointmentID: Int, status: String) {
        // 更新 Room
        appointmentsDao.updateStatus(appointmentID, status)

        // 同步 Firebase
        val snapshot = db.collection("appointments")
            .whereEqualTo("appointmentID", appointmentID.toLong()) // 找到对应的文档
            .get()
            .await()

        for (doc in snapshot.documents) {
            doc.reference.update("status", status)
        }
    }



}