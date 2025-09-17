package com.example.mylastapp.ui.rooms.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class Users(
    @PrimaryKey
    @ColumnInfo(name = "ic")
    val ic: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "phone")
    val phone: String = "",

    @ColumnInfo(name = "age")
    val age: Int = 0,

    @ColumnInfo(name = "weight")
    val weight: Double = 0.0,

    @ColumnInfo(name = "height")
    val height: Double = 0.0,

    @ColumnInfo(name = "address")
    val address: String = "",

    @ColumnInfo(name = "gender")
    val gender: String = "",

    @ColumnInfo(name = "medicalHistory")
    val medicalHistory: String = ""
)

