package com.example.mylastapp.ui.userTheme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.ui.rooms.entity.Doctors
import com.example.mylastapp.ui.theme.ArimaTypography
import com.example.mylastapp.ui.theme.MyLastAppTheme
import kotlin.math.roundToInt

@Composable
fun UserConfirmBookScreen(
    modifier: Modifier = Modifier,
    doctor: Doctors?,
    dates: List<String>,
    times: List<String>,
    selectedDate: String,
    onChangeSelectedDate: (String) -> Unit,
    selectedTime: String,
    onChangeSelectedTime: (String) -> Unit,
    remark: String,
    onChangeRemark: (String) -> Unit,
    onConfirmClick: () -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = doctor?.name ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )

                val rating = doctor?.rating ?: 0.0
                val star = "⭐".repeat(rating.roundToInt())

                Text(
                    "${doctor?.specialty} $star ${doctor?.rating}",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )


                Text(
                    "\"To care for the brain is to care for the essence of who we are.\"",
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Color.Gray
                )
            }
        }

        Text(
            "Date",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(dates) { date ->
                FilterChip(
                    selected = date == selectedDate,
                    onClick = { onChangeSelectedDate(date) },
                    label = { Text(date) },
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
        }

        Text(
            "Time Schedule",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        if (times.isEmpty()) {
            Text(
                text = "No available times",
                color = Color.Red,
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(times) { time ->
                    FilterChip(
                        selected = time == selectedTime,
                        onClick = { onChangeSelectedTime(time) },
                        label = {
                            Text(if (time == selectedTime) "$time ✓" else time)
                        },
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
        }

        Text(
            "Remarks",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        OutlinedTextField(
            value = remark,
            onValueChange = onChangeRemark,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            placeholder = {
                Text("Add your note...")
            },
            maxLines = Int.MAX_VALUE,
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = onConfirmClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 80.dp, vertical = 10.dp),
            enabled = selectedDate.isNotEmpty() && selectedTime.isNotEmpty(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C8B3))
        ) {
            Text(
                "Confirm Booking",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

    }
}


@Composable
fun UserSuccessBookScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = Color(0xFF00C8B3), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Check",
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(700, delayMillis = 200))
        ) {
            Text(
                text = "Booking Confirmed",
                fontSize = 32.sp,
                style = ArimaTypography.displayMedium,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(900, delayMillis = 300))
        ) {
            Text(
                text = "Your appointment has been scheduled successfully",
                fontSize = 18.sp,
                style = ArimaTypography.displaySmall,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000, delayMillis = 400))
        ) {
            Button(
                onClick = onBackClick
            ) {
                Text("Back to Home")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun UserConfirmBookScreenPreview() {
    MyLastAppTheme {
        UserConfirmBookScreen(
            modifier = Modifier
                .fillMaxSize(),
            doctor = Doctors(
                name = "Dr. John Doe",
                degree = "MBBS, MD",
                specialty = "Cardiologist",
                year = 10,
                language = "English, Spanish",
                quote = "",
                dayOff = "Monday",
                rating = 4.5,
            ),
            dates = listOf("2023-07-01", "2023-07-02", "2023-07-03"),
            times = listOf("09:00", "10:00", "11:00"),
            selectedDate = "2023-07-01",
            onChangeSelectedDate = {},
            selectedTime = "09:00 AM",
            onChangeSelectedTime = {},
            remark = "",
            onChangeRemark = {},
            onConfirmClick = {}
        )
    }
}

@Preview
@Composable
fun SuccessPreview() {
    MyLastAppTheme {
        UserSuccessBookScreen(
            modifier = Modifier
                .fillMaxSize(),
            onBackClick = {}
        )
    }
}