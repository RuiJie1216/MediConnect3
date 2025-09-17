package com.example.mylastapp.ui.rooms.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mylastapp.ui.rooms.entity.Doctors
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: Doctors)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<Doctors>)

    @Update
    suspend fun updateDoctor(doctor: Doctors)

    @Update
    suspend fun updateDoctors(doctors: List<Doctors>)

    @Delete
    suspend fun deleteDoctor(doctor: Doctors)

    @Delete
    suspend fun deleteDoctors(doctors: List<Doctors>)

    @Query("SELECT * FROM doctors_table")
    fun getAllDoctors(): Flow<List<Doctors>>

    @Query("DELETE FROM doctors_table")
    suspend fun deleteAllDoctors()

    @Query("SELECT * FROM doctors_table WHERE id = :id")
    suspend fun getDoctorById(id: String): Doctors?

    @Transaction
    suspend fun replaceAllDoctors(doctors: List<Doctors>) {
        deleteAllDoctors()
        insertDoctors(doctors)
    }

}