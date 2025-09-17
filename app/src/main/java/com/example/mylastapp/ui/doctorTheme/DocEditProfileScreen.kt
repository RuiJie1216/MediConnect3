package com.example.mylastapp.ui.doctorTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.ui.userTheme.specialties


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocEditProfileScreen(
    name: String,
    onCDocName: (String) -> Unit,

    phone: String,
    onCDocPhone: (String) -> Unit,

    degree: String,
    onCDocDegree: (String) -> Unit,

    specialty: String,
    onCDocSpecialty: (String) -> Unit,

    year: Int,
    onCDocYear: (Int) -> Unit,

    quote: String,
    onCDocQuote: (String) -> Unit,

    language: String, // 这里 UI 显示时用 "English + Malay"
    onCDocLanguage: (List<String>) -> Unit,

    dayOff: String,
    onCDocDayOff: (List<String>) -> Unit,

    onCamClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelEditClick: () -> Unit
) {

    var yearDropdownExpanded by remember { mutableStateOf(false) }
    val yearOptions = (1..50).map { it }

    var languageDropdownExpanded by remember { mutableStateOf(false) }
    val allLanguages = listOf("English", "Malay", "Chinese", "Tamil")
    val selectedLanguages = remember(language) { mutableStateOf(language.split(", ").filter { it.isNotBlank() }) }

    var dayOffDropdownExpanded by remember { mutableStateOf(false) }
    val allDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val selectedDayOff = remember(dayOff) { mutableStateOf(dayOff.split(", ").filter { it.isNotBlank() }) }

    var specialtyDropdownExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // Avatar + Camera
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0xFF0EC4B3)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DR",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = onCamClick,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Change photo",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onCDocName,
                label = { Text("Full name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            if (name.isBlank()) {
                Text(
                    text = "* Name cannot be empty",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }

            OutlinedTextField(
                value = phone,
                onValueChange = onCDocPhone,
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            if (phone.isBlank()) {
                Text(
                    text = "* Phone cannot be empty",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            } else if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
                Text(
                    text = "* Invalid phone number",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }
            OutlinedTextField(
                value = degree,
                onValueChange = onCDocDegree,
                label = { Text("Degree") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            if (degree.isBlank()) {
                Text(
                    text = "* Degree cannot be empty",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }
            ExposedDropdownMenuBox(
                expanded = specialtyDropdownExpanded,
                onExpandedChange = { specialtyDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = specialty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Specialty") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = specialtyDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = specialtyDropdownExpanded,
                    onDismissRequest = { specialtyDropdownExpanded = false }
                ) {
                    specialties.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.name) },
                            leadingIcon = { Icon(item.icon, contentDescription = null) },
                            onClick = {
                                onCDocSpecialty(item.name)
                                specialtyDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            // Year of Practice (Dropdown)
            ExposedDropdownMenuBox(
                expanded = yearDropdownExpanded,
                onExpandedChange = { yearDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = year.toString(),
                    onValueChange = {},
                    label = { Text("Years of Practice") },
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                )
                DropdownMenu(
                    expanded = yearDropdownExpanded,
                    onDismissRequest = { yearDropdownExpanded = false },
                    modifier = Modifier
                        .heightIn(max = 250.dp)
                ) {
                    yearOptions.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year.toString()) },
                            onClick = {
                                onCDocYear(year)
                                yearDropdownExpanded = false
                            }
                        )
                    }
                }
            }


            // language (Dropdown)

            ExposedDropdownMenuBox(
                expanded = languageDropdownExpanded,
                onExpandedChange = { languageDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedLanguages.value.joinToString(", "), // UI 用 +
                    onValueChange = {},
                    label = { Text("Language(s)") },
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = languageDropdownExpanded,
                    onDismissRequest = { languageDropdownExpanded = false }
                ) {
                    allLanguages.forEach { lang ->
                        val isSelected = selectedLanguages.value.contains(lang)
                        DropdownMenuItem(
                            text = {
                                Row {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = null // 交给 onClick 控制
                                    )
                                    Text(lang)
                                }
                            },
                            onClick = {
                                val newLang = if (isSelected) {
                                    selectedLanguages.value - lang
                                } else {
                                    selectedLanguages.value + lang
                                }
                                selectedLanguages.value = newLang
                                onCDocLanguage(newLang) // 回传到上层
                            }
                        )
                    }
                }
            }
            if (selectedLanguages.value.isEmpty()) {
                Text(
                    text = "* Select at least one language",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }
            //Days
            ExposedDropdownMenuBox(
                expanded = dayOffDropdownExpanded,
                onExpandedChange = { dayOffDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedDayOff.value.joinToString(", "), // UI 用 +
                    onValueChange = {},
                    label = { Text("Day(s) Off") },
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = dayOffDropdownExpanded,
                    onDismissRequest = { dayOffDropdownExpanded = false }
                ) {
                    allDays.forEach { day ->
                        val isSelected = selectedDayOff.value.contains(day)
                        DropdownMenuItem(
                            text = {
                                Row {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = null
                                    )
                                    Text(day)
                                }
                            },
                            onClick = {
                                val newDays = if (isSelected) {
                                    selectedDayOff.value - day
                                } else {
                                    selectedDayOff.value + day
                                }
                                selectedDayOff.value = newDays
                                onCDocDayOff(newDays)
                            }
                        )
                    }
                }
            }
            if (selectedDayOff.value.isEmpty()) {
                Text(
                    text = "* Select at least one day",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }
            OutlinedTextField(
                value = quote,
                onValueChange = onCDocQuote,
                label = { Text("Quote") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
            if (quote.isBlank()) {
                Text(
                    text = "* Quote cannot be empty",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onSaveClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0EC4B3)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Save")
                }

                OutlinedButton(
                    onClick = onCancelEditClick,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Cancel", color = Color(0xFF0EC4B3)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDocEditProfileScreen() {
    DocEditProfileScreen(
        name = "zzz",
        onCDocName = {},
        phone = "",
        onCDocPhone = {},
        degree = "",
        onCDocDegree = {},
        specialty = "",
        onCDocSpecialty = {},
        year = 0,
        onCDocYear = {},
        quote = "Healing minds, restoring hope.",
        onCDocQuote = {},
        language = "",
        onCDocLanguage = {},
        dayOff = "",
        onCDocDayOff = {},
        onCamClick = {},
        onSaveClick = {},
        onCancelEditClick = {}
    )
}
