package com.example.mylastapp.ui.rooms.repo

import android.util.Log
import com.example.mylastapp.ui.rooms.dao.DoctorsDao
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DoctorsRepo(
    private val doctorsDao: DoctorsDao
) {
    private val db = FirebaseFirestore.getInstance()

    fun getAllDoctors() = doctorsDao.getAllDoctors()

    suspend fun insert(doctor: Doctors) = doctorsDao.insertDoctor(doctor)

    suspend fun insertAll(doctors: List<Doctors>) = doctorsDao.insertDoctors(doctors)

    suspend fun update(doctor: Doctors) = doctorsDao.updateDoctor(doctor)

    suspend fun updateAll(doctors: List<Doctors>) = doctorsDao.updateDoctors(doctors)

    suspend fun delete(doctor: Doctors) = doctorsDao.deleteDoctor(doctor)

    suspend fun deleteAll() = doctorsDao.deleteAllDoctors()

    suspend fun getDoctorById(id: String): Doctors? = doctorsDao.getDoctorById(id)

    suspend fun syncFromFirebase(): Boolean = try {
        val snapshot = db.collection("doctors").get().await()
        val doctors = snapshot.documents.map { doc ->
            Doctors(
                id = doc.getString("id") ?: "",
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: "",
                phone = doc.getString("phone") ?: "",
                degree = doc.getString("degree") ?: "",
                specialty = doc.getString("specialty") ?: "",
                year = doc.getLong("year")?.toInt() ?: 0,
                currentHospital = doc.getString("currentHospital") ?: "",
                quote = doc.getString("quote") ?: "",
                language = (doc.get("languages") as? List<*>)?.joinToString(", ") { it as? String ?: "" } ?: "",
                dayOff = (doc.get("dayOff") as? List<*>)?.joinToString(", ") { it as? String ?: "" } ?: "",
                rating = doc.getDouble("rating") ?: 0.0,
            )


        }
        Log.d("SyncDebug", "Doctors: $doctors")
        Log.d("SyncDebug", "Doctor inserted: ${doctors.size}")
        doctorsDao.replaceAllDoctors(doctors)
        Log.d("SyncDebug", "Doctors synced from Firebase successfully")
        true
    }
    catch (e: Exception) {
        Log.e("SyncDebug", "Error syncing from Firebase", e)
        false
    }



}