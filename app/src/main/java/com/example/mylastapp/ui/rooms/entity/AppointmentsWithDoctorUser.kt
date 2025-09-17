package com.example.mylastapp.ui.rooms.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AppointmentsWithDoctorUser(
    @Embedded(prefix = "")
    val appointment: Appointments,

    @Relation(
        parentColumn = "doctorID",
        entityColumn = "id"
    )
    val doctor: Doctors,

    @Relation(
        parentColumn = "userIC",
        entityColumn = "ic"
    )
    val user: Users
)
