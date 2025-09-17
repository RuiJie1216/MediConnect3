package com.example.mylastapp.ui.doctorTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylastapp.ui.rooms.entity.Patients
import com.example.mylastapp.ui.rooms.entity.PatientsWithUsers
import com.example.mylastapp.ui.rooms.entity.Users
import com.example.mylastapp.ui.theme.MyLastAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocPatientsScreen(
    modifier: Modifier = Modifier,
    doctorPatients: List<PatientsWithUsers?>,
    searchQuery: String,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onPatientDetailClick: (PatientsWithUsers?) -> Unit
) {

    Box(
        modifier = modifier

    ) {

        val filteredPatients = if (searchQuery.isBlank()) {
            doctorPatients
        } else {
            doctorPatients.filter {
                it?.users?.name?.contains(searchQuery, ignoreCase = true) == true
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            items(filteredPatients) { patient ->
                PatientItem(
                    name = patient?.users?.name ?: "",
                    condition = patient?.patient?.complaint ?: "",
                    onClick = { onPatientDetailClick(patient) }
                )
            }
        }

    }

}

@Composable
fun PatientItem(
    name: String,
    condition: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Patient Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, style = MaterialTheme.typography.titleMedium)
                Text(text = condition, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientPreview() {
    MyLastAppTheme {
        DocPatientsScreen(
            doctorPatients = listOf(
                PatientsWithUsers(
                    patient = Patients(
                        patientIC = "001",
                        doctorID = "D123",
                        complaint = "Fever, Cough"
                    ),
                    users = Users(
                        ic = "001",
                        name = "Albert Wong Jim Yam"
                    )
                ),
                PatientsWithUsers(
                    patient = Patients(
                        patientIC = "002",
                        doctorID = "D123",
                        complaint = "Diabetes, High Cholesterol"
                    ),
                    users = Users(
                        ic = "002",
                        name = "Albert Yong Hong Lin"
                    )
                )
            ),
            searchQuery = "",
            onBackClick = {},
            onAddClick = {},
            onPatientDetailClick = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
