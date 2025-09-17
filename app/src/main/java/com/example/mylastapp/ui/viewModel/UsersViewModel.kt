package com.example.mylastapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylastapp.ui.rooms.entity.MedicalReminder
import com.example.mylastapp.ui.rooms.entity.Users
import com.example.mylastapp.ui.rooms.repo.MedicalRepo
import com.example.mylastapp.ui.rooms.repo.UsersRepo
import com.example.mylastapp.ui.uiState.SignUpUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.content.Context
import android.os.Build
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import com.example.mylastapp.ui.userTheme.reminderSystem.ReminderNotification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.provider.Settings
import androidx.core.net.toUri
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.example.mylastapp.ui.rooms.repo.DoctorsRepo
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.Job


class UsersViewModel(
    private val usersRepo: UsersRepo,
    private val medicalRepo: MedicalRepo,
    private val doctorsRepo: DoctorsRepo
): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val users: StateFlow<List<Users>> =
        usersRepo.getAllUsers().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentUser = MutableStateFlow<Users?>(null)
    val currentUser: StateFlow<Users?> = _currentUser

    private val _selectedDoctor = MutableStateFlow<Doctors?>(null)
    val selectedDoctor: StateFlow<Doctors?> = _selectedDoctor

    private var reminderJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val allMedicalReminders: StateFlow<List<MedicalReminder>> =
        _currentUser.flatMapLatest { user ->
            if (user != null) {
                medicalRepo.getRemindersByIc(user.ic)
            } else {
                flowOf(emptyList())
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        syncFirebase()
    }

    fun observeReminders(context: Context) {
        viewModelScope.launch {
            medicalRepo.syncFromFirebase()
            allMedicalReminders.collectLatest { reminders ->
                Log.d("UsersViewModel", "Collected ${reminders.size} reminders")
                if (reminders.isNotEmpty()) {
                    scheduleReminders(context, reminders)
                }
            }
        }
    }

    fun syncFirebase() {
        viewModelScope.launch {
            usersRepo.syncFromFirebase()
        }
    }

    fun syncFirebaseMedical() {
        viewModelScope.launch {
            medicalRepo.syncFromFirebase()
        }
    }

    fun startReminderScheduler(context: Context) {
        reminderJob?.cancel() // 先取消旧的
        reminderJob = viewModelScope.launch {
            allMedicalReminders.collectLatest { reminders ->
                cancelReminders(context, reminders)
                scheduleReminders(context, reminders)
            }
        }
    }

    fun stopReminderScheduler() {
        reminderJob?.cancel()
        reminderJob = null
    }

    fun getUserByIc(ic: String, onResult: (Users?) -> Unit) {
        viewModelScope.launch {
            val user = usersRepo.getUserByIc(ic)
            onResult(user)
        }
    }

    fun login(ic: String, pwd: String, context: Context, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = usersRepo.getUserByIc(ic)

            if (user == null) {
                onResult(false, "IC not found")
                return@launch
            }

            auth.signInWithEmailAndPassword(user.email, pwd)
                .addOnSuccessListener {
                    _currentUser.value = user

                    syncFirebaseMedical()
                    startReminderScheduler(context)

                    onResult(true, "Login successfully")
                }
                .addOnFailureListener { exception ->
                    onResult(false, "Error: ${exception.message}")
                }

        }

    }

    fun signup(signUpUiState: SignUpUiState, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(signUpUiState.email, signUpUiState.pwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = Users(
                        ic = signUpUiState.ic,
                        name = signUpUiState.name,
                        email = signUpUiState.email,
                        phone = signUpUiState.phone
                    )
                    viewModelScope.launch {
                        usersRepo.insert(user)
                    }

                    val userMap = mapOf(
                        "ic" to signUpUiState.ic,
                        "name" to signUpUiState.name,
                        "email" to signUpUiState.email,
                        "phone" to signUpUiState.phone
                    )
                    db.collection("users").document()
                        .set(userMap)
                        .addOnSuccessListener {
                            onResult(true, "Sign up successfully")
                        }
                        .addOnFailureListener { exception ->
                            onResult(false, "Error: ${exception.message}")
                        }

                    onResult(true, "Sign up successfully")
                } else {
                    onResult(false, "Error: ${task.exception?.message}")
                }
            }
    }

    fun checkUser(ic: String, email: String, phone: String, onResult: (Boolean, String) -> Unit) {
        db.collection("users")
            .whereEqualTo("ic", ic)
            .get()
            .addOnSuccessListener { icSnapshot ->
                if (!icSnapshot.isEmpty) {
                    onResult(false, "IC already exists")
                    return@addOnSuccessListener
                }
                db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { emailSnapshot ->
                        if (!emailSnapshot.isEmpty) {
                            onResult(false, "Email already exists")
                            return@addOnSuccessListener
                        }
                        db.collection("users")
                            .whereEqualTo("phone", phone)
                            .get()
                            .addOnSuccessListener { phoneSnapshot ->
                                if (!phoneSnapshot.isEmpty) {
                                    onResult(false, "Phone already exists")
                                    return@addOnSuccessListener
                                }

                                onResult(true, "Success")
                            }
                    }
            }
    }


    fun update(user: Users) {
        viewModelScope.launch {
            try {
                usersRepo.update(user)
                _currentUser.value = user

                val userMap = mapOf(
                    "name" to user.name,
                    "phone" to user.phone,
                    "age" to user.age,
                    "address" to user.address,
                    "gender" to user.gender,
                    "weight" to user.weight,
                    "height" to user.height,
                    "medicalHistory" to user.medicalHistory
                )

                db.collection("users")
                    .whereEqualTo("ic", user.ic)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            document.reference.update(userMap)
                        }
                        Log.d("UsersViewModel", "User updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("UsersViewModel", "Failed to update user in Firestore: ${e.message}", e)
                    }
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Failed to update user: ${e.message}", e)
            }
        }
    }

    fun logout(context: Context) {
        auth.signOut()
        stopReminderScheduler()
        cancelReminders(context, allMedicalReminders.value)
        _currentUser.value = null
    }


    private fun scheduleReminders(context: Context, reminders: List<MedicalReminder>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

        reminders.forEach { reminder ->
            val dateStrings = reminder.date.split(",")
            dateStrings.forEach { dateStr ->
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH)
                    val date = sdf.parse("$dateStr ${reminder.time}") ?: return@forEach

                    Log.d("AlarmTest", "Parsed reminder date: $date")

                    if (date.time < System.currentTimeMillis()) return@forEach

                    val intent = Intent(context, ReminderNotification::class.java).apply {
                        putExtra("title", "Medication Reminder")
                        putExtra("message", "Times to take your medical\n ${reminder.name} - ${reminder.dose} tablets")
                    }

                    val alarmID = (reminder.id.toString() + dateStr).hashCode()
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        alarmID, // 确保每个日期唯一
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    try {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.time, pendingIntent)
                    } catch (e: SecurityException) {
                        Log.e("AlarmTest", "Exact alarm not allowed", e)
                    }
                } catch (e: Exception ) {
                    Log.e("AlarmTest", "Failed to parse date '${dateStr}'", e)
                }
            }
        }
    }

    private fun cancelReminders(context: Context, reminders: List<MedicalReminder>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        reminders.forEach { reminder ->
            val dateStrings = reminder.date.split(",")
            dateStrings.forEach { dateStr ->
                val intent = Intent(context, ReminderNotification::class.java)

                val alarmID = (reminder.id.toString() + dateStr).hashCode()
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)

                val checkPI = PendingIntent.getBroadcast(
                    context,
                    reminder.id + dateStr.hashCode(),
                    intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )
                if (checkPI == null) {
                    Log.d("AlarmTest", "Cancel success for ${reminder.name} at $dateStr")
                } else {
                    Log.d("AlarmTest", "Cancel fail for ${reminder.name} at $dateStr")
                }
            }
        }
    }

    fun setSelectedDoctor(doctors: Doctors) {
        viewModelScope.launch {
            _selectedDoctor.value = doctors
        }
    }
}