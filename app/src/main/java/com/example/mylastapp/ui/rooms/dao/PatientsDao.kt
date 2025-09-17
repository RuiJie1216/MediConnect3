package com.example.mylastapp.ui.rooms.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mylastapp.ui.rooms.entity.Patients
import com.example.mylastapp.ui.rooms.entity.PatientsWithUsers
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patients: Patients)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(patients: List<Patients>)

    @Update
    suspend fun update(patients: Patients)

    @Update
    suspend fun updateAll(patients: List<Patients>)

    @Delete
    suspend fun delete(patients: Patients)

    @Delete
    suspend fun deleteAll(patients: List<Patients>)

    @Query("SELECT * FROM patients_table")
    fun getAllPatients(): Flow<List<Patients>>

    @Query("DELETE FROM patients_table")
    suspend fun deleteAllPatients()

    @Transaction
    suspend fun replaceAllPatients(patients: List<Patients>) {
        deleteAllPatients()
        insertAll(patients)
    }

    @Transaction
    @Query("SELECT * FROM patients_table WHERE doctorID = :doctorID")
    fun getPatientsWithUsersByDoctorID(doctorID: String): Flow<List<PatientsWithUsers>>

    @Query("SELECT * FROM patients_table WHERE patientIC = :userIC")
    suspend fun getPatientByUserIC(userIC: String): Patients?


}