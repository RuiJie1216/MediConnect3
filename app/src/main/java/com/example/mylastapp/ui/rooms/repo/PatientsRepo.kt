package com.example.mylastapp.ui.rooms.repo

import android.util.Log
import com.example.mylastapp.ui.rooms.dao.PatientsDao
import com.example.mylastapp.ui.rooms.entity.Patients
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PatientsRepo(
    private val patientsDao: PatientsDao
) {
    private val db = FirebaseFirestore.getInstance()

    fun getAllPatients() = patientsDao.getAllPatients()

    suspend fun insert(patient: Patients) = patientsDao.insert(patient)

    suspend fun insertAll(patients: List<Patients>) = patientsDao.insertAll(patients)

    suspend fun update(patient: Patients) = patientsDao.update(patient)

    suspend fun updateAll(patients: List<Patients>) = patientsDao.updateAll(patients)

    suspend fun delete(patient: Patients) = patientsDao.delete(patient)

    suspend fun deleteAll() = patientsDao.deleteAllPatients()

    fun getPatientsWithUsersByDoctorID(doctorID: String) = patientsDao.getPatientsWithUsersByDoctorID(doctorID)

    suspend fun getPatientByUserIC(userIC: String) = patientsDao.getPatientByUserIC(userIC)
    suspend fun syncFromFirebase(): Boolean = try {
        val snapshot = db.collection("patientInfo").get().await()
        val patients = snapshot.documents.map { doc ->
            Patients(
                patientID = doc.getLong("patientID")?.toInt() ?: 0,
                doctorID = doc.getString("doctorID") ?: "",
                patientIC = doc.getString("patientIC") ?: "",
                complaint = doc.getString("complaint") ?: "",
                hpi = doc.getString("hpi") ?: "",
                medicationHistory = doc.getString("medication_history") ?: "",
                followUp = doc.getString("followUp") ?: ""
            )

        }
        Log.d("SyncDebug", "Patients: $patients")
        Log.d("SyncDebug", "Patient inserted: ${patients.size}")
        patientsDao.replaceAllPatients(patients)
        Log.d("SyncDebug", "Patients synced from Firebase successfully")
        true
    } catch (e: Exception) {
        Log.e("SyncDebug", "Error syncing from Firebase", e)
        false
    }



}