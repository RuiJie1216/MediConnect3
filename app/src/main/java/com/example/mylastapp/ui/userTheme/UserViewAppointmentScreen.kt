package com.example.mylastapp.ui.userTheme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.ui.rooms.entity.Appointments
import com.example.mylastapp.ui.rooms.entity.AppointmentsWithDoctorUser
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.example.mylastapp.ui.rooms.entity.Users
import com.example.mylastapp.ui.theme.MyLastAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserViewAppointmentScreen(
    modifier: Modifier = Modifier,
    appointments: List<AppointmentsWithDoctorUser?>
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "View Appointment",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 15.dp)
        ) {
            items(appointments) { appointment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Doctor: ${appointment?.doctor?.name}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Date: ${formatDateWithDay(appointment?.appointment?.date ?: "")}")
                        Text(text = "Time: ${appointment?.appointment?.time}")
                        Text(text = "Complaint: ${appointment?.appointment?.complaint}")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            when (appointment?.appointment?.status) {
                                "Pending" -> {
                                    Text(text = "Status: ")
                                    Text(text = appointment.appointment.status, color = Color.Yellow, fontWeight = FontWeight.Bold)
                                }
                                "Check-in" -> {
                                    Text(text = "Status: ")
                                    Text(text = appointment.appointment.status, color = Color.Green, fontWeight = FontWeight.Bold)
                                }
                                "Missed" -> {
                                    Text(text = "Status: ")
                                    Text(text = appointment.appointment.status, color = Color.Red, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

            }
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateWithDay(dateStr: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateStr, formatter)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        "$dayOfWeek, $date"
    } catch (e: Exception) {
        dateStr
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    MyLastAppTheme {
        UserViewAppointmentScreen(
            appointments = listOf(
                AppointmentsWithDoctorUser(
                    appointment = Appointments(
                        appointmentID = 1234,
                        userIC = "U12345",
                        doctorID = "D001",
                        date = "2025-09-27",
                        time = "10:30 AM",
                        complaint = "Headache and dizziness",
                        status = "Check-in"
                    ),
                    doctor = Doctors(
                        id = "D001",
                        name = "Dr. Alice Tan",
                        email = "alice.tan@hospital.com",
                        phone = "012-3456789",
                        degree = "MBBS",
                        specialty = "Neurology",
                        year = 12,
                        currentHospital = "General Hospital KL",
                        quote = "Caring for the mind and body.",
                        language = "English, Mandarin",
                        dayOff = "Sunday"
                    ),
                    user = Users(
                        ic = "U12345",
                        name = "John Lim",
                        email = "john.lim@gmail.com",
                        phone = "017-2233445",
                        age = 29,
                        weight = 68.5,
                        height = 175.0,
                        address = "Kuala Lumpur, Malaysia",
                        gender = "Male",
                        medicalHistory = "Asthma"
                    )
                )
            ),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}