package com.example.mylastapp.ui.doctorTheme

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DocAppointmentScreen(
    modifier: Modifier = Modifier,
    docAppointments: List<AppointmentsWithDoctorUser>,
    selectedDate: LocalDate,
    onChangeSelectedDate: (LocalDate) -> Unit,
    onAppointmentClick: (AppointmentsWithDoctorUser) -> Unit
) {

    Column(
        modifier = modifier,
    ) {
        DateSelectorBar(
            selectedDate = selectedDate,
            onDateSelected = onChangeSelectedDate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp, vertical = 8.dp),
            text = "Select Date"
        )

        if (!docAppointments.isEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                items(docAppointments) {
                    appointment ->
                    if (appointment.appointment.status.equals("Pending", ignoreCase = true)) {
                        AppointmentCard(
                            appointment = appointment,
                            onClick = {
                                onAppointmentClick(appointment)
                            }
                        )
                    }
                }
            }
        }



    }
    
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentCard(
    appointment: AppointmentsWithDoctorUser,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val today = LocalDate.now()


    Card(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F9F9)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appointment.user.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(150.dp),
                    color = Color.Black
                )

                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1565C0), // 边框颜色
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = Color(0xFFBBDEFB), // 背景色
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = appointment.appointment.time,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1565C0) // 文字颜色
                    )
                }

            }

            Text(
                text = "Complaint:\n${appointment.appointment.complaint}",
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .width(200.dp),
                color = Color.Gray
            )


            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.Start),
                enabled = appointment.appointment.date == today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            ) {
                Text("Check In")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelectorBar(
    text: String,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    OutlinedTextField(
        value = selectedDate.format(formatter),
        onValueChange = {}, // readOnly, 不需要更新
        label = { Text(text) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = {
                val calendar = Calendar.getInstance()
                calendar.set(
                    selectedDate.year,
                    selectedDate.monthValue - 1,
                    selectedDate.dayOfMonth
                )

                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                // 限制只能选今天及未来
                datePickerDialog.datePicker.minDate = System.currentTimeMillis()

                datePickerDialog.show()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date"
                )
            }
        },
        modifier = modifier
    )
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DocAppointmentPreview() {
    MyLastAppTheme {
        DocAppointmentScreen(
            docAppointments = listOf(
                AppointmentsWithDoctorUser(
                    appointment = Appointments(
                        appointmentID = 1234,
                        userIC = "U12345",
                        doctorID = "D001",
                        date = "27/09/2025",
                        time = "10:30 AM",
                        complaint = "Headache and dizziness"
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
                .fillMaxSize(),
            selectedDate = LocalDate.now(),
            onChangeSelectedDate = {},
            onAppointmentClick = {}
        )

    }
}