package com.example.mylastapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylastapp.ui.doctorTheme.MedicationUiState
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.example.mylastapp.ui.rooms.entity.MedicalReminder
import com.example.mylastapp.ui.rooms.entity.Patients
import com.example.mylastapp.ui.rooms.entity.PatientsWithUsers
import com.example.mylastapp.ui.rooms.entity.Users
import com.example.mylastapp.ui.rooms.repo.DoctorsRepo
import com.example.mylastapp.ui.rooms.repo.MedicalRepo
import com.example.mylastapp.ui.rooms.repo.PatientsRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DoctorsViewModel(
    private val doctorsRepo: DoctorsRepo,
    private val patientsRepo: PatientsRepo,
    private val medicalRepo: MedicalRepo
): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _currentDoctor = MutableStateFlow<Doctors?>(null)
    val currentDoctor: StateFlow<Doctors?> = _currentDoctor

    private val _patients = MutableStateFlow<List<PatientsWithUsers?>>(emptyList())
    val patients: StateFlow<List<PatientsWithUsers?>> = _patients

    private val _currentPatient = MutableStateFlow<PatientsWithUsers?>(null)
    val currentPatient: StateFlow<PatientsWithUsers?> = _currentPatient

    val doctors: StateFlow<List<Doctors>> =
        doctorsRepo.getAllDoctors().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        syncDoctorFirebase()
    }

    fun syncDoctorFirebase() {
        viewModelScope.launch {
            doctorsRepo.syncFromFirebase()

        }
    }

    fun syncPatientsFirebase() {
        viewModelScope.launch {
            patientsRepo.syncFromFirebase()
        }
    }

    fun getDoctorById(id: String, onResult: (Doctors?) -> Unit) {
        viewModelScope.launch {
            val doctor = doctorsRepo.getDoctorById(id)
            onResult(doctor)
        }
    }

    fun login(id: String, pwd: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val doctor = doctorsRepo.getDoctorById(id)
            Log.d("LoginDebug", "doctor from db: $doctor")

            if (doctor == null) {
                onResult(false, "ID not found")
                return@launch
            }

            auth.signInWithEmailAndPassword(doctor.email, pwd)
                .addOnSuccessListener {
                    _currentDoctor.value = doctor
                    syncPatientsFirebase()
                    onResult(true, "Login successfully")
                }
                .addOnFailureListener { exception ->
                    onResult(false, "Error: ${exception.message}")
                }

        }
    }

    fun logout() {
        auth.signOut()
        _currentDoctor.value = null
    }

    fun insertReminder(reminder: MedicalReminder) {
        viewModelScope.launch {
            medicalRepo.insertReminder(reminder)

            val reminderMap = mapOf(
                "id" to reminder.id,
                "ic" to reminder.ic,
                "medicalName" to reminder.name,
                "dose" to reminder.dose,
                "instruction" to reminder.instruction,
                "date" to reminder.date,
                "time" to reminder.time
            )

            try {
                db.collection("medicalReminder")
                    .add(reminderMap)
                    .await()
                Log.d("DoctorViewModel", "Reminder inserted successfully")
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Failed to insert reminder in Firestore: ${e.message}", e)
            }

        }
    }



    fun updateOrCreatePatient(userIC: String, complaint: String, hpi: String, medicationHistory: List<MedicationUiState>, followUp: String) {
        viewModelScope.launch {
            try {
                val existing = patientsRepo.getPatientByUserIC(userIC)

                val patient = if (existing != null) {
                    val updated = existing.copy(
                        complaint = complaint,
                        hpi = hpi,
                        medicationHistory = medicationHistory.joinToString("\n") { med ->
                            "${med.medicineName} x ${med.dose} capsules"
                        },
                        followUp = followUp
                    )
                    patientsRepo.update(updated)
                    updated
                } else {
                    val newPatient = Patients(
                        doctorID = _currentDoctor.value?.id ?: "",
                        patientIC = userIC,
                        complaint = complaint,
                        hpi = hpi,
                        medicationHistory = medicationHistory.joinToString("\n") { med ->
                            "${med.medicineName} x ${med.dose} capsules"
                        },
                        followUp = followUp
                    )
                    patientsRepo.insert(newPatient)
                    newPatient
                }

                val patientMap = mapOf(
                    "patientID" to patient.patientID,
                    "doctorID" to patient.doctorID,
                    "patientIC" to patient.patientIC,
                    "complaint" to patient.complaint,
                    "hpi" to patient.hpi,
                    "medication_history" to patient.medicationHistory,
                    "followUp" to patient.followUp
                )

                val snapshots = db.collection("patientInfo")
                    .whereEqualTo("patientID", patient.patientID)
                    .get()
                    .await()

                if (!snapshots.isEmpty) {
                    // 已经存在 -> 更新
                    val docRef = snapshots.documents[0].reference
                    docRef.set(patientMap) // 或者 docRef.update(patientMap)
                } else {
                    // 不存在 -> 新建
                    db.collection("patientInfo").add(patientMap)
                }
                Log.d("DoctorViewModel", "Patient updated successfully")
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Failed to get patient: ${e.message}", e)
            }
        }
    }



    fun getPatientsByDoctorID(doctorID: String) {
        viewModelScope.launch {
            patientsRepo.getPatientsWithUsersByDoctorID(doctorID).collectLatest { patientsWithUsers ->
                _patients.value = patientsWithUsers
            }
        }
    }


    fun setCurrentPatient(patient: PatientsWithUsers?) {
        _currentPatient.value = patient
    }

    fun update(doctor: Doctors, languages: List<String>, daysOff: List<String>) {
        viewModelScope.launch {

            val doctorForRoom = doctor.copy(
                language = languages.joinToString(", "),
                dayOff = daysOff.joinToString(", ")
            )
            doctorsRepo.update(doctorForRoom)
            _currentDoctor.value = doctorForRoom
            try {
                val doctorMap = mapOf(
                    "name" to doctor.name,
                    "email" to doctor.email,
                    "phone" to doctor.phone,
                    "degree" to doctor.degree,
                    "specialty" to doctor.specialty,
                    "quote" to doctor.quote,
                    "year" to doctor.year,
                    "languages" to languages,
                    "dayOff" to daysOff
                )

                db.collection("doctors")
                    .whereEqualTo("id", doctor.id)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            document.reference.update(doctorMap)
                        }
                        Log.d("DoctorsViewModel", "Doctor updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("DoctorsViewModel", "Failed to update doctor in Firestore: ${e.message}", e)
                    }
            } catch (e: Exception) {
                Log.e("DoctorsViewModel", "Failed to update doctor: ${e.message}", e)
            }
        }
    }


    fun updatePatient(patient: Patients) {
        viewModelScope.launch {
            // update room
            patientsRepo.update(patient)

            try {
                val snapshots = db.collection("patientInfo")
                    .whereEqualTo("patientID", patient.patientID)
                    .get()
                    .await()

                if (!snapshots.isEmpty) {
                    val snapshot = snapshots.documents[0]
                    val patientMap = mapOf(
                        "patientID" to patient.patientID,
                        "doctorID" to patient.doctorID,
                        "patientIC" to patient.patientIC,
                        "complaint" to patient.complaint,
                        "hpi" to patient.hpi,
                        "medication_history" to patient.medicationHistory,
                        "followUp" to patient.followUp
                    )

                    snapshot.reference.update(patientMap)
                    Log.d("DoctorViewModel", "Patient updated successfully")
                }
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Failed to update patient in Firestore: ${e.message}", e)
            }
        }
    }



}