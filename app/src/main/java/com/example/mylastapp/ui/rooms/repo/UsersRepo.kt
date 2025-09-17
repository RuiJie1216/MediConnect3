package com.example.mylastapp.ui.rooms.repo

import android.util.Log
import com.example.mylastapp.ui.rooms.dao.UsersDao
import com.example.mylastapp.ui.rooms.entity.Users
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class UsersRepo(
    private val usersDao: UsersDao
) {
    private val db = FirebaseFirestore.getInstance()

    fun getAllUsers(): Flow<List<Users>> = usersDao.getAllUsers()

    suspend fun insert(user: Users) = usersDao.insertUser(user)

    suspend fun insertAll(users: List<Users>) = usersDao.insertUsers(users)

    suspend fun update(user: Users) = usersDao.updateUser(user)

    suspend fun updateAll(users: List<Users>) = usersDao.updateUsers(users)

    suspend fun delete(user: Users) = usersDao.deleteUser(user)

    suspend fun deleteAll() = usersDao.deleteAllUsers()

    suspend fun getUserByIc(ic: String): Users? = usersDao.getUserByIc(ic)

    suspend fun syncFromFirebase(): Boolean = try {
        val snapshot = db.collection("users").get().await()
        val users = snapshot.documents.map { doc ->
            Users(
                ic = doc.getString("ic") ?: "",
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: "",
                phone = doc.getString("phone") ?: "",
                age = doc.getLong("age")?.toInt() ?: 0,
                weight = doc.getDouble("weight") ?: 0.0,
                height = doc.getDouble("height") ?: 0.0,
                address = doc.getString("address") ?: "",
                gender = doc.getString("gender") ?: "",
                medicalHistory = doc.getString("medicalHistory") ?: ""
            )


        }
        Log.d("SyncDebug", "User inserted: ${users.size}")
        usersDao.replaceAllUsers(users)
        Log.d("SyncDebug", "Users synced from Firebase successfully")
        true
    } catch (e: Exception) {
        Log.e("SyncDebug", "Error syncing from Firebase", e)
        false
    }


}