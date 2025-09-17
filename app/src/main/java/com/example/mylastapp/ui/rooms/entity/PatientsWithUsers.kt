package com.example.mylastapp.ui.rooms.entity

import androidx.room.Embedded
import androidx.room.Relation


data class PatientsWithUsers (
    @Embedded(prefix = "")
    val patient: Patients,
    @Relation(
        parentColumn = "patientIC",
        entityColumn = "ic"
    )
    val users: Users?
)
