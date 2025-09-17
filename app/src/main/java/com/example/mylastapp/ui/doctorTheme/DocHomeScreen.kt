package com.example.mylastapp.ui.doctorTheme

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.ui.rooms.entity.AppointmentsWithDoctorUser
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocHomeScreen(
    modifier: Modifier = Modifier,
    onAppointmentClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPatientClick: () -> Unit,
    appointments: List<AppointmentsWithDoctorUser?>
) {
    val today = LocalDate.now()
    val currentTime = LocalTime.now()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

    val todayAppointments = appointments.filter { app ->
        try {
            Log.d("DocHomeScreen", "app: $app")
            val date = LocalDate.parse(app?.appointment?.date, dateFormatter)
            val time = LocalTime.parse(app?.appointment?.time, timeFormatter)
            date == today && time.isAfter(currentTime)
        } catch (e: Exception) {
            false
        }
    }.sortedBy { app ->
        LocalTime.parse(app?.appointment?.time, timeFormatter)
    }.take(3)

    Column(
        modifier = modifier
    ) {
        Row {
            Button(
                onClick = onAppointmentClick,
                modifier = Modifier
                    .padding(8.dp)
                    .height(125.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFCBFFFC),
                    contentColor = Color.Black
                )
            ) {
                Text("Appointment List")
            }

            Column {
                Button(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC7FFEC),
                        contentColor = Color.Black
                    )

                ) {
                    Text("My Profile")
                }

                Button(
                    onClick = onPatientClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5EFFC9),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Patients Information")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Current Appointment",
            style = MaterialTheme.typography.titleMedium,
            fontStyle = FontStyle.Italic,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(8.dp))

        Column(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95F)
                    .height(150.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .background(Color(0xFFEFFAF9), shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📅 Today: ${today.format(dateFormatter)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }

                    HorizontalDivider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .padding(8.dp),
                    ) {
                        if (todayAppointments.isEmpty()) {
                            Text("No more appointments today", color = Color.Gray)
                        } else {
                            Column {
                                todayAppointments.forEach { app ->
                                    Text(
                                        text = "${app?.user?.name} - ${app?.appointment?.time}",
                                        modifier = Modifier.padding(bottom = 6.dp),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}