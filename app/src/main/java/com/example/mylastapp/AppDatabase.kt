package com.example.mylastapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mylastapp.ui.rooms.dao.AppointmentsDao
import com.example.mylastapp.ui.rooms.dao.DoctorsDao
import com.example.mylastapp.ui.rooms.dao.MedicalDao
import com.example.mylastapp.ui.rooms.dao.PatientsDao
import com.example.mylastapp.ui.rooms.dao.UsersDao
import com.example.mylastapp.ui.rooms.entity.Appointments
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.example.mylastapp.ui.rooms.entity.MedicalReminder
import com.example.mylastapp.ui.rooms.entity.Patients
import com.example.mylastapp.ui.rooms.entity.Users

@Database(
    entities = [
        Users::class,
        Doctors::class,
        Patients::class,
        MedicalReminder::class,
        Appointments::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun doctorsDao(): DoctorsDao
    abstract fun patientsDao(): PatientsDao
    abstract fun medicalDao(): MedicalDao

    abstract fun appointmentsDao(): AppointmentsDao


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.execSQL("PRAGMA foreign_keys=ON;")
                    }
                })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}