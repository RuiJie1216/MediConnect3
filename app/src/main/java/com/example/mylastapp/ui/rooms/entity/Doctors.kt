package com.example.mylastapp.ui.rooms.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors_table")
data class Doctors(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "phone")
    val phone: String = "",

    @ColumnInfo(name = "degree")
    val degree: String = "",

    @ColumnInfo(name = "specialty")
    val specialty: String = "",

    @ColumnInfo(name = "year")
    val year: Int = 0,

    @ColumnInfo(name = "current_hospital")
    val currentHospital: String = "",

    @ColumnInfo(name = "quote")
    val quote: String = "",

    @ColumnInfo(name = "languages")
    val language: String = "",

    @ColumnInfo(name = "day_off")
    val dayOff: String = "",

    @ColumnInfo(name = "rating")
    val rating: Double = 0.0
)
