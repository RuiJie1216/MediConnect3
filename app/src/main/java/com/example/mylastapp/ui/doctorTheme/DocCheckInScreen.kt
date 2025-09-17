package com.example.mylastapp.ui.doctorTheme

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.ui.rooms.entity.Appointments
import com.example.mylastapp.ui.rooms.entity.AppointmentsWithDoctorUser
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.example.mylastapp.ui.rooms.entity.Patients
import com.example.mylastapp.ui.rooms.entity.PatientsWithUsers
import com.example.mylastapp.ui.rooms.entity.Users
import com.example.mylastapp.ui.theme.MyLastAppTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries.localTime
import java.util.Calendar
import java.util.Locale

data class MedicationUiState(
    var medicineName: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var time: String = "",
    var dose: String = "",
    var repeatType: String = "Daily",
    var weeklyDays: List<String> = emptyList(),
    var interval: String = "0",
    var instruction: String = ""
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DocCheckInScreen(
    modifier: Modifier = Modifier,
    appointment: AppointmentsWithDoctorUser?,
    hpi: String,
    onChangeHpi: (String) -> Unit,
    followUp: String,
    onChangeFollowUp: (String) -> Unit,
    onCheckIn: (List<MedicationUiState>) -> Unit
) {
    var medications by remember { mutableStateOf(listOf(MedicationUiState())) }

    val isInputValid = remember(hpi, followUp, medications) {
        hpi.isNotBlank() && followUp.isNotBlank() &&
                medications.all { med ->
                    val validTime = try {
                        if (med.time.isBlank()) {
                            false
                        } else {
                            val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
                            val inputTime = LocalTime.parse(med.time, formatter)
                            !inputTime.isBefore(LocalTime.now())
                        }
                    } catch (e: Exception) {
                        false
                    }

                    med.medicineName.isNotBlank() &&
                            med.dose.isNotBlank() &&
                            med.dose.toLongOrNull() != null &&
                            med.dose.toLong() > 0 &&
                            med.instruction.isNotBlank() &&
                            validTime &&
                            when (med.repeatType) {
                                "Daily" -> med.startDate.isNotBlank() && med.endDate.isNotBlank()
                                "Weekly" -> med.weeklyDays.isNotEmpty() && med.interval.isNotBlank() && med.interval.toLongOrNull() != null
                                "Monthly" -> med.interval.isNotBlank() && med.interval.toLongOrNull() != null
                                else -> true
                            }
                }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = appointment?.appointment?.complaint ?: "",
            onValueChange = {},
            label = { Text("Chief Complaint") },
            readOnly = true,
            modifier = Modifier
                .padding(vertical = 5.dp)
        )


        OutlinedTextField(
            value = hpi,
            onValueChange = onChangeHpi,
            label = { Text("History of Present Illness (HPI)") },
            modifier = Modifier
                .padding(vertical = 5.dp)
        )


        OutlinedTextField(
            value = followUp,
            onValueChange = onChangeFollowUp,
            label = { Text("Follow-up") },
            modifier = Modifier
                .padding(vertical = 5.dp)
        )

        SetMedicationHistory(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 20.dp)
                .align(Alignment.Start),
            medications = medications,
            onAdded = {
                medications = medications + listOf(MedicationUiState())
            },
            onUpdated = { index, medication ->
                medications = medications.toMutableList().also { it[index] = medication }
            },
            onRemove = { index ->
                medications = medications.toMutableList().also { it.removeAt(index) }
            }
        )

        Button(
            onClick = { onCheckIn(medications) } ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp, vertical = 10.dp),
            enabled = isInputValid
        ) {
            Text(
                text = "Confirm Check-in"
            )
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetMedicationHistory(
    modifier: Modifier = Modifier,
    medications: List<MedicationUiState>,
    onAdded: () -> Unit,
    onUpdated: (index: Int, medication: MedicationUiState) -> Unit,
    onRemove: (index: Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Medication Setting",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 5.dp)
            )

            Button(
                onClick = onAdded,
                modifier = Modifier
            ) {
                Text(
                    text = "Add",
                    textAlign = TextAlign.Center,
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(medications) { index, medication ->
                Card(
                    modifier = Modifier
                        .width(360.dp)
                        .padding(horizontal = 5.dp)
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    MedicationItem(
                        index = index,
                        medication = medication,
                        onUpdate = { onUpdated(index, it) },
                        onRemove = { onRemove(index) }
                    )
                }
            }
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicationItem(
    index: Int,
    medication: MedicationUiState,
    onUpdate: (MedicationUiState) -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)


    var startDate by remember {
        mutableStateOf(medication.startDate.ifEmpty { LocalDate.now().toString() })
    }
    var endDate by remember {
        mutableStateOf(medication.endDate.ifEmpty { LocalDate.now().toString() })
    }

    var time by remember {
        mutableStateOf(medication.time.ifEmpty {
            LocalTime.now()
                .plusHours(1)
                .format(formatter)
        })
    }

    LaunchedEffect(Unit) {
        onUpdate(
            medication.copy(
                startDate = startDate,
                endDate = endDate,
                time = time
            )
        )
    }

    val repeatOptions = listOf("Daily", "Weekly", "Monthly")

    fun updateStartDate(newStart: String) {
        startDate = newStart
        val start = LocalDate.parse(newStart)
        val end = LocalDate.parse(endDate)
        if (end.isBefore(start)) {
            endDate = newStart
        }
        onUpdate(medication.copy(startDate = startDate, endDate = endDate))
    }

    fun updateEndDate(newEnd: String) {
        val end = LocalDate.parse(newEnd)
        val start = LocalDate.parse(startDate)
        endDate = if (end.isBefore(start)) startDate else newEnd
        onUpdate(medication.copy(endDate = endDate))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Medication ${index + 1}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 5.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Medicine Name
            OutlinedTextField(
                value = medication.medicineName,
                onValueChange = { onUpdate(medication.copy(medicineName = it)) },
                singleLine = true,
                label = { Text("Medicine Name", fontSize = 12.sp) },
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )

            // Dose
            OutlinedTextField(
                value = medication.dose,
                onValueChange = { onUpdate(medication.copy(dose = it)) },
                singleLine = true,
                label = { Text("Dose per time", fontSize = 12.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
        }

        // Instruction
        OutlinedTextField(
            value = medication.instruction,
            onValueChange = { onUpdate(medication.copy(instruction = it)) },
            label = { Text("Instruction", fontSize = 12.sp) },
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        )

        // time
        OutlinedTextField(
            value = time,
            onValueChange = {},
            label = { Text("Set Time", fontSize = 12.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Alarm,
                    contentDescription = "Pick Time",
                    modifier = Modifier.clickable {
                        val calendar = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hour: Int, minute: Int ->
                                val localTime = LocalTime.of(hour, minute)
                                val formatted = localTime.format(formatter)
                                time = formatted
                                onUpdate(medication.copy(time = formatted))
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    }
                )
            }
        )


        // Repeat Type
        Text(
            text = "Repeat Type",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            repeatOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onUpdate(medication.copy(repeatType = option)) }
                ) {
                    RadioButton(
                        selected = medication.repeatType == option,
                        onClick = { onUpdate(medication.copy(repeatType = option)) }
                    )
                    Text(
                        text = option,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // 根据 repeatType 显示不同输入
        when (medication.repeatType) {
            "Daily" -> {
                // Daily → 显示开始结束日期
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DateSelectorBar(
                        selectedDate = LocalDate.parse(startDate),
                        onDateSelected = {
                            updateStartDate(it.toString())
                        },
                        modifier = Modifier
                            .weight(1f),
                        text = "Start Date"
                    )
                    Text(text = " : ", fontSize = 14.sp)
                    DateSelectorBar(
                        selectedDate = LocalDate.parse(endDate),
                        onDateSelected = {
                            updateEndDate(it.toString())
                        },
                        modifier = Modifier
                            .weight(1f),
                        text = "End Date"
                    )
                }
            }

            "Weekly" -> {
                // Weekly → 选择星期 + 持续周数
                Text("Select days to take medicine:")
                val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                Row {
                    weekdays.take(4).forEach { day ->
                        Row(
                            modifier = Modifier.width(80.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val checked = medication.weeklyDays.contains(day)
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    val newDays = if (it) medication.weeklyDays + day else medication.weeklyDays - day
                                    onUpdate(medication.copy(weeklyDays = newDays))
                                }
                            )
                            Text(day, fontSize = 13.sp)
                        }
                    }
                }
                Row {
                    weekdays.slice(4..6).forEach { day ->
                        Row(
                            modifier = Modifier.width(80.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val checked = medication.weeklyDays.contains(day)
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    val newDays = if (it) medication.weeklyDays + day else medication.weeklyDays - day
                                    onUpdate(medication.copy(weeklyDays = newDays))
                                }
                            )
                            Text(day, fontSize = 13.sp)
                        }
                    }
                }
                OutlinedTextField(
                    value = medication.interval,
                    onValueChange = { onUpdate(medication.copy(interval = it)) },
                    label = { Text("Duration (weeks)", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            "Monthly" -> {
                // Monthly → 持续几个月
                OutlinedTextField(
                    value = medication.interval,
                    onValueChange = { onUpdate(medication.copy(interval = it)) },
                    label = { Text("Duration (months)", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Remove button
        Button(onClick = onRemove) {
            Text("Remove Medication")
        }
    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DocCheckInScreenPreview() {
    MyLastAppTheme {
        DocCheckInScreen(
            modifier = Modifier
                .fillMaxSize(),
            appointment = AppointmentsWithDoctorUser(
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
            ),
            hpi = "testHPI",
            onChangeHpi = {},
            followUp = "testFollowUp",
            onChangeFollowUp = {},
            onCheckIn = {}

        )
    }
}