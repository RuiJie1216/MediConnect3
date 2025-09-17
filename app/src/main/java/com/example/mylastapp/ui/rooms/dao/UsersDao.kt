package com.example.mylastapp.ui.rooms.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mylastapp.ui.rooms.entity.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<Users>)

    @Update
    suspend fun updateUser(user: Users)

    @Update
    suspend fun updateUsers(users: List<Users>)

    @Delete
    suspend fun deleteUser(user: Users)

    @Delete
    suspend fun deleteUsers(users: List<Users>)

    @Query("SELECT * FROM users_table")
    fun getAllUsers(): Flow<List<Users>>

    @Query("DELETE FROM users_table")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM users_table WHERE ic = :ic")
    suspend fun getUserByIc(ic: String): Users?

    @Transaction
    suspend fun replaceAllUsers(users: List<Users>) {
        deleteAllUsers()
        insertUsers(users)
    }

}