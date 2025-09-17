package com.example.mylastapp.ui.rooms.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "patients_table",
    foreignKeys = [
        ForeignKey(
            entity = Doctors::class,
            parentColumns = ["id"],
            childColumns = ["doctorID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Users::class,
            parentColumns = ["ic"],
            childColumns = ["patientIC"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("doctorID"), Index("patientIC")]
)
data class Patients(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "patientID")
    val patientID: Int = 0,

    @ColumnInfo(name = "doctorID")
    val doctorID: String = "",

    @ColumnInfo(name = "patientIC")
    val patientIC: String = "",

    @ColumnInfo(name = "complaint")
    val complaint: String = "",

    @ColumnInfo(name = "hpi")
    val hpi: String = "",

    @ColumnInfo(name = "medicationHistory")
    val medicationHistory: String = "",

    @ColumnInfo(name = "followUp")
    val followUp: String = ""
)
