package com.example.mylastapp.ui.rooms.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.Update
import com.example.mylastapp.ui.rooms.entity.Appointments
import com.example.mylastapp.ui.rooms.entity.AppointmentsWithDoctorUser
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointments): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<Appointments>)

    @Update
    suspend fun updateAppointment(appointment: Appointments)

    @Update
    suspend fun updateAppointments(appointments: List<Appointments>)

    @Delete
    suspend fun deleteAppointment(appointment: Appointments)

    @Delete
    suspend fun deleteAppointments(appointments: List<Appointments>)

    @Query("DELETE FROM appointments_table")
    suspend fun deleteAllAppointments()

    @Query("SELECT * FROM appointments_table")
    fun getAllAppointments(): Flow<List<Appointments>>

    @Transaction
    suspend fun replaceAllAppointments(appointments: List<Appointments>) {
        deleteAllAppointments()
        insertAppointments(appointments)
    }


    @Transaction
    @Query("SELECT * FROM appointments_table WHERE userIC = :userIC")
    fun getAppointmentsByUserIC(userIC: String): Flow<List<AppointmentsWithDoctorUser>>

    @Transaction
    @Query("SELECT * FROM appointments_table WHERE doctorID = :doctorID")
    fun getAppointmentsByDoctorID(doctorID: String): Flow<List<AppointmentsWithDoctorUser>>

    @Transaction
    @Query("SELECT * FROM appointments_table WHERE doctorID = :doctorID AND date = :date")
    fun getAppointmentsByDoctorIDAndDate(doctorID: String, date: String): Flow<List<AppointmentsWithDoctorUser>>

    @Query("UPDATE appointments_table SET status = :status WHERE appointmentID = :appointmentID")
    suspend fun updateStatus(appointmentID: Int, status: String)

    @Query("SELECT time FROM appointments_table WHERE doctorID = :doctorId AND date = :date")
    suspend fun getBookedTimes(doctorId: String, date: String): List<String>
}