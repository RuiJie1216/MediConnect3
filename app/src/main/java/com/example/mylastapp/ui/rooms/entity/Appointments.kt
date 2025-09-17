package com.example.mylastapp.ui.rooms.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "appointments_table",
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["ic"],
            childColumns = ["userIC"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Doctors::class,
            parentColumns = ["id"],
            childColumns = ["doctorID"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userIC"), Index("doctorID")]
)
data class Appointments(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "appointmentID")
    val appointmentID: Int = 0,

    @ColumnInfo(name = "userIC")
    val userIC: String = "",

    @ColumnInfo(name = "doctorID")
    val doctorID: String = "",

    @ColumnInfo(name = "date")
    val date: String = "",

    @ColumnInfo(name = "time")
    val time: String = "",

    @ColumnInfo(name = "complaint")
    val complaint: String = "",

    @ColumnInfo(name = "status")
    val status: String = ""
)


