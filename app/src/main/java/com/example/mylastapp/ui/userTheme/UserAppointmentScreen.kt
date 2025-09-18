package com.example.mylastapp.ui.userTheme

import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mylastapp.AppScreen
import com.example.mylastapp.R
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.example.mylastapp.ui.theme.MyLastAppTheme
import com.google.common.math.LinearTransformation.vertical
import kotlin.math.roundToInt


data class Specialty(
    val id: String,
    val name: String,
    val icon: ImageVector
)

val specialties = listOf(
    Specialty("surgery", "Surgery", Icons.Default.Healing),
    Specialty("internal_medicine", "Internal Medicine", Icons.Default.MedicalServices),
    Specialty("neurology", "Neurology", Icons.Default.AccountCircle),
    Specialty("radiology", "Radiology", Icons.Default.CameraAlt),
    Specialty("pediatrics", "Pediatrics", Icons.Default.ChildCare),
    Specialty("orthopaedic", "Orthopaedic", Icons.Default.Accessibility),
    Specialty("urology", "Urology", Icons.Default.Person),
    Specialty("dermatology", "Dermatology", Icons.Default.Face),
    Specialty("hematology", "Hematology", Icons.Default.Bloodtype),
    Specialty("endocrinology", "Endocrinology", Icons.Default.Science)
)

enum class ViewState {
    SPECIALTIES,
    DOCTORS
}

@Composable
fun UserAppointmentScreen(
    modifier: Modifier = Modifier,
    filteredSpecialties: List<Specialty>,
    filteredDoctors: List<Doctors>,
    onDoctorCardClick: (Doctors) -> Unit,
    onSpecialtyCardClick: (Specialty) -> Unit,
    currentViewState: ViewState,
    onChangeViewState: (ViewState) -> Unit,
    query: String,
    onChangeQuery: (String) -> Unit,
    currentScreen: AppScreen,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var currentDoctorPage by remember { mutableStateOf(1) }
    val itemsPerPage = 2

    //change specialty or search, return to page 1
    LaunchedEffect(filteredDoctors) {
        currentDoctorPage = 1
    }

    val totalDoctorPages = (filteredDoctors.size + itemsPerPage - 1) / itemsPerPage
    val currentDoctorItems = if(filteredDoctors.isEmpty()){
        emptyList()
    } else {
        val safePage = when {
            totalDoctorPages <= 0 -> 1
            currentDoctorPage > totalDoctorPages -> totalDoctorPages
            currentDoctorPage < 1 -> 1
            else -> currentDoctorPage
    }
        filteredDoctors.drop((safePage - 1) * itemsPerPage).take(itemsPerPage)
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = if (currentViewState == ViewState.DOCTORS) {
                        Modifier.border(1.dp, Color(0xFF00C8B3), RoundedCornerShape(40.dp))
                    } else {
                        Modifier
                    },
                    onClick = {
                        if (currentViewState == ViewState.DOCTORS) {
                            onChangeQuery("")
                            onChangeViewState(ViewState.SPECIALTIES)
                            currentDoctorPage = 1
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentViewState == ViewState.SPECIALTIES) Color(
                            0xFF00C8B3
                        ) else Color.White
                    )
                ) {
                    Text(
                        "Specialty",
                        color = if (currentViewState == ViewState.SPECIALTIES) Color.White else Color.DarkGray
                    )
                }

                Button(
                    modifier = if (currentViewState == ViewState.SPECIALTIES) {
                        Modifier.border(1.dp, Color(0xFF00C8B3), RoundedCornerShape(40.dp))
                    } else {
                        Modifier
                    },
                    onClick = {
                        if (currentViewState == ViewState.SPECIALTIES) {
                            onChangeQuery("")
                            onChangeViewState(ViewState.DOCTORS)
                            currentDoctorPage = 1
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentViewState == ViewState.DOCTORS) Color(0xFF00C8B3) else Color.White
                    )
                ) {
                    Text(
                        "Doctor",
                        color = if (currentViewState == ViewState.DOCTORS) Color.White else Color.DarkGray
                    )
                }
            }

            OutlinedTextField(
                value = query,
                onValueChange = onChangeQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text(
                        text = if (currentViewState == ViewState.SPECIALTIES)
                            "Search by specialty"
                        else
                            "Search by doctor name, specialty or languages"
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF00C8B3)
                    )
                },
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = { onChangeQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (currentViewState == ViewState.SPECIALTIES) {
                if (query.isNotBlank()) {
                    Text(
                        text = "Search results for \"$query\" (${filteredSpecialties.size} found)",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 15.dp, bottom = 8.dp),
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "Select Specialty",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 15.dp, bottom = 8.dp),
                        color = Color.Black
                    )
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 100.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(filteredSpecialties) { specialty ->
                        SpecialtyCard(
                            specialty = specialty,
                            onClick = { onSpecialtyCardClick(specialty) })
                    }
                }

            } else {
                if (query.isNotBlank()) {
                    Text(
                        text = "Search results for \"$query\" (${filteredDoctors.size} found)",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 15.dp, bottom = 8.dp),
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "Available Doctors",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 15.dp, bottom = 8.dp),
                        color = Color.Black
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 100.dp, start = 16.dp, end = 16.dp)
                ) {
                    currentDoctorItems.forEach { doctor ->
                        DoctorCard(
                            doctor = doctor,
                            onClick = { onDoctorCardClick(doctor) },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(93.dp))

                    if (totalDoctorPages > 1) {
                        PageBar(
                            currentPage = currentDoctorPage,
                            totalPages = totalDoctorPages,
                            onPageChange = { newPage -> currentDoctorPage = newPage },
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // ===== constant bar bottom =====
        UserChooseBar(
            chooseBar = currentScreen,
            onTurnClick = {
                if (it == AppScreen.UserHome)
                    onHomeClick()
                else
                    onProfileClick()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
fun PageBar(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp,horizontal = 10.dp)
    ) {
        IconButton(
            onClick = { if (currentPage > 1) onPageChange(currentPage - 1) },
            enabled = currentPage > 1,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Previous")
        }

        for (i in 1..totalPages) {
            Button(
                onClick = { onPageChange(i) },
                modifier = Modifier
                    .size(36.dp)
                    .padding(horizontal = 2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (i == currentPage) Color(0xFF00C8B3) else Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = i.toString(),
                    color = if (i == currentPage) Color.White else Color.Black
                )
            }
        }

        IconButton(
            onClick = { if (currentPage < totalPages) onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next")
        }
    }
}

@Composable
fun SpecialtyCard(
    specialty: Specialty,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        border = BorderStroke(1.dp, Color(0xFF00C8B3)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Icon(
                imageVector = specialty.icon,
                contentDescription = specialty.name,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF00C8B3)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = specialty.name,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DoctorCard(
    doctor: Doctors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Doctor Avatar",
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF00C8B3)
                )

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        doctor.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color(0xFF333333)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Specialty: ${doctor.specialty}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF666666),
                            fontSize = 14.sp
                        )
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Languages: ${doctor.language}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF666666),
                            fontSize = 14.sp
                        )
                    )

                    Spacer(Modifier.height(4.dp))

                    val rating = doctor?.rating ?: 0.0
                    val star = "⭐".repeat(rating.roundToInt())

                    Text(
                        "Rating: $star ${doctor.rating}/5.0",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF666666),
                            fontSize = 14.sp
                        )
                    )
                }

            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C8B3)
                )
            ) {
                Text(
                    "Select Date",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun UsApPreview() {
    MyLastAppTheme {
        UserAppointmentScreen(
            modifier = Modifier
                .fillMaxSize(),
            filteredSpecialties = specialties,
            filteredDoctors = listOf(
                Doctors(
                    name = "Dr. John Doe",
                    degree = "MBBS, MD",
                    specialty = "Surgery",
                    year = 10,
                    language = "English, Spanish",
                    quote = "",
                    dayOff = "Monday",
                    rating = 4.5,
                ),
                Doctors(
                    name = "Dr. Jane Smith",
                    degree = "MD, PhD",
                    specialty = "Neurology",
                    year = 15,
                    language = "English, Mandarin",
                    quote = "",
                    dayOff = "Tuesday",
                    rating = 4.8,
                ),
                Doctors(
                    name = "Dr. Robert Brown",
                    degree = "MBBS",
                    specialty = "Radiology",
                    year = 8,
                    language = "English, French",
                    quote = "",
                    dayOff = "Wednesday",
                    rating = 4.3,
                ),
            ),
            onSpecialtyCardClick = {},
            currentViewState = ViewState.DOCTORS,
            onChangeViewState = {},
            query = "",
            onChangeQuery = {},
            currentScreen = AppScreen.UserAppointment,
            onHomeClick = {},
            onProfileClick = {},
            onDoctorCardClick = {}
        )
    }
}
