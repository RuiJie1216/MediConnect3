package com.example.mylastapp.ui.rooms.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medical_reminder",
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["ic"],
            childColumns = ["user_ic"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_ic")]
)
data class MedicalReminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "user_ic")
    val ic: String = "",

    @ColumnInfo(name = "medicine_name")
    val name: String = "",

    @ColumnInfo(name = "medicine_dose")
    val dose: Int = 0,

    @ColumnInfo(name = "instruction")
    val instruction: String ="",

    @ColumnInfo(name = "time")
    val time: String = "", // 08:00 AM

    @ColumnInfo(name = "date")
    val date: String = ""// yyyy-MM-dd
)
