package com.example.mylastapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylastapp.ui.rooms.entity.Appointments
import com.example.mylastapp.ui.rooms.entity.AppointmentsWithDoctorUser
import com.example.mylastapp.ui.rooms.repo.AppointmentsRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AppointmentsViewModel(
    private val appointmentsRepo: AppointmentsRepo
): ViewModel() {

    val db = FirebaseFirestore.getInstance()

    private val _appointments = MutableStateFlow<List<AppointmentsWithDoctorUser>>(emptyList())
    val appointments: StateFlow<List<AppointmentsWithDoctorUser>> = _appointments

    private val _currentAppointment = MutableStateFlow<AppointmentsWithDoctorUser?>(null)
    val currentAppointment: StateFlow<AppointmentsWithDoctorUser?> = _currentAppointment


    init {
        checkDateTimeLoop()
    }

    private fun checkDateTimeLoop() {
        viewModelScope.launch {
            while (true) {
                updateAppointmentStatus()
                delay(60000)
            }
        }
    }

    private suspend fun updateAppointmentStatus() {
        val allAppointments = appointmentsRepo.getAllAppointments().first()

        val now = Calendar.getInstance().time

        allAppointments.forEach { appointment ->
            if (appointment.status != "Pending") return@forEach

            val dateTimeStr = "${appointment.date} ${appointment.time}"
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH)
            val appointmentDate = sdf.parse(dateTimeStr)

            if (appointmentDate != null) {
                val diffMillis = now.time - appointmentDate.time
                if (diffMillis > 60 * 60 * 1000) {
                    updateStatus(appointment.appointmentID, "Missed")
                }
            }
        }
    }

    fun syncAppointmentsFirebase() {
        viewModelScope.launch {
            appointmentsRepo.syncFromFirebase()
        }
    }

    fun setCurrentAppointment(appointment: AppointmentsWithDoctorUser) {
        _currentAppointment.value = appointment
    }

    fun updateStatus(appointmentID: Int, status: String) {
        viewModelScope.launch {
            _currentAppointment.value = _currentAppointment.value?.let { current ->
                if (current.appointment.appointmentID == appointmentID) {
                    current.copy(
                        appointment = current.appointment.copy(status = status)
                    )
                } else current
            }

            _appointments.value = _appointments.value.map { item ->
                if (item.appointment.appointmentID == appointmentID) {
                    item.copy(
                        appointment = item.appointment.copy(status = status)
                    )
                } else item
            }

            appointmentsRepo.updateStatus(appointmentID, status)
        }
    }


    fun getAppointmentsByDoctorIDAndDate(doctorID: String, date: String) {
        viewModelScope.launch {
            appointmentsRepo.getAppointmentsByDoctorIDAndDate(doctorID, date).collect { appointments ->
                _appointments.value = appointments
            }
        }
    }

    suspend fun getBookedTimes(doctorId: String, date: String): List<String> {
        return appointmentsRepo.getBookedTimes(doctorId, date)
    }

    fun getAppointmentByUserIC(userIC: String) {
        viewModelScope.launch {
            appointmentsRepo.getAppointmentsByUserIC(userIC).collect { appointments ->
                _appointments.value = appointments
            }
        }
    }




    fun insert(appointment: Appointments) {
        viewModelScope.launch {
            val id = appointmentsRepo.insert(appointment)

            val appointmentMap = mapOf(
                "appointmentID" to id,
                "doctorID" to appointment.doctorID,
                "userIC" to appointment.userIC,
                "date" to appointment.date,
                "time" to appointment.time,
                "status" to appointment.status,
                "complaint" to appointment.complaint
            )

            db.collection("appointments").document()
                .set(appointmentMap)
                .addOnSuccessListener {
                    Log.d("SyncDebug", "Appointment inserted to Firebase successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("SyncDebug", "Error inserting appointment to Firebase", exception)
                }
        }
    }
}