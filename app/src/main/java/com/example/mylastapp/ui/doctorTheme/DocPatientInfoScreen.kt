package com.example.mylastapp.ui.doctorTheme

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylastapp.ui.rooms.entity.Patients
import com.example.mylastapp.ui.rooms.entity.PatientsWithUsers
import com.example.mylastapp.ui.rooms.entity.Users
import com.example.mylastapp.ui.theme.MyLastAppTheme
import kotlin.math.exp

@Composable
fun DocPatientInfoScreen(
    modifier: Modifier = Modifier,
    patient: PatientsWithUsers?
){
    val personalInfo = "Name: ${patient?.users?.name}\n" +
                "IC: ${patient?.patient?.patientIC}\n" +
                "Age: ${patient?.users?.age}\n" +
                "Gender: ${patient?.users?.gender}\n" +
                "Weight: ${patient?.users?.weight}\n" +
                "Height: ${patient?.users?.height}\n"


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        Text(
            text = "Patient ID: ${patient?.patient?.patientID}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                PatientInfoItem("Personal Details", personalInfo)
                PatientInfoItem("Chief Complaint", patient?.patient?.complaint ?: "")
                PatientInfoItem("History of Present Illness (HPI)", patient?.patient?.hpi ?: "")
                PatientInfoItem("Medication History", patient?.patient?.medicationHistory ?: "")
                PatientInfoItem("Follow Up", patient?.patient?.followUp ?: "")
            }
        }
    }
}

@Composable
fun PatientInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
                IconButton(
                    onClick = {expanded = !expanded}
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (expanded) {
                Text(
                    text = value,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DocPatientInfoPreview() {
    MyLastAppTheme {
        DocPatientInfoScreen(
            patient = PatientsWithUsers(
                patient = Patients(
                    patientIC = "001",
                    doctorID = "D123",
                    complaint = "Fever, Cough"
                ),
                users = Users(
                    ic = "001",
                    name = "Albert Wong Jim Yam"
                )
            )
        )
    }
}