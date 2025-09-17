package com.example.mylastapp.ui.doctorTheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.ui.theme.MyLastAppTheme

@Composable
fun DocEditPatientScreen(
    modifier: Modifier = Modifier,
    id: Int,
    complaint: String,
    onChangeComplaint: (String) -> Unit,
    hpi: String,
    onChangeHpi: (String) -> Unit,
    medicationHistory: String,
    onChangeMedicationHistory: (String) -> Unit,
    followUp: String,
    onChangeFollowUp: (String) -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Patient ID: $id",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(vertical = 12.dp)
        )


        EditPatientComplaint(
            value = complaint,
            onValueChange = onChangeComplaint,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )

        EditPatientHpi(
            value = hpi,
            onValueChange = onChangeHpi,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )

        EditPatientMedicationHistory(
            value = medicationHistory,
            onValueChange = onChangeMedicationHistory,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )

        EditPatientFollowUp(
            value = followUp,
            onValueChange = onChangeFollowUp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )


        Button(
            onClick = onConfirm,
            modifier = Modifier
                .padding(top = 120.dp)
        ) {
            Text(
                text = "Confirm",
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }


    }
}

@Composable
fun EditPatientFollowUp(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Follow Up") },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        maxLines = 3
    )
}

@Composable
fun EditPatientMedicationHistory(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Medication History") },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        maxLines = 3
    )
}


@Composable
fun EditPatientHpi(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("History of Present Illness (HPI)") },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        maxLines = 2
    )
}

@Composable
fun EditPatientComplaint(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Complaint") },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        singleLine = true
    )
}



@Preview(showBackground = true)
@Composable
fun DocEditPatientPreview() {
    MyLastAppTheme {
        DocEditPatientScreen(
            id = 1234,
            complaint = "test",
            onChangeComplaint = {},
            hpi = "testHPI",
            onChangeHpi = {},
            medicationHistory = "testMH",
            onChangeMedicationHistory = {},
            followUp = "testFU",
            onChangeFollowUp = {},
            modifier = Modifier
                .fillMaxSize(),
            onConfirm = {}
        )
    }
}