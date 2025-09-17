package com.example.mylastapp.ui.userTheme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.AppScreen
import com.example.mylastapp.ui.rooms.entity.MedicalReminder
import com.example.mylastapp.ui.theme.BalooTypography
import com.example.mylastapp.ui.theme.InterTypography
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserMedicReminderScreen(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onChangeSelectedDate: (LocalDate) -> Unit,
    reminders: List<MedicalReminder>,
    currentScreen: AppScreen,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAppointmentClick: () -> Unit,
) {


    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Medical Reminder",
                style = InterTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontSize = 25.sp
            )
        }

        DayOfMonthItems(
            modifier = Modifier,
            selectedDate = selectedDate,
            onDateSelected = onChangeSelectedDate
        )

        HorizontalDivider(
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reminders) { reminder ->
                    ReminderItem(reminder)
                }
            }
        }


        HorizontalDivider(
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        UserChooseBar(
            chooseBar = currentScreen,
            onTurnClick = {
                if (it == AppScreen.UserHome) {
                    onHomeClick()
                }
                else if (it == AppScreen.UserProfile) {
                    onProfileClick()
                }
                else {
                    onAppointmentClick()

                }
            }
        )


    }
}

@Composable
fun ReminderItem(
    reminder: MedicalReminder
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = reminder.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Dosage: ${reminder.dose}",
                fontSize = 16.sp
            )
            Text(
                text = "Instruction: ${reminder.instruction}",
                fontSize = 16.sp
            )
            Text(
                text = "Time: ${reminder.time}",
                fontSize = 16.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DayOfMonthItems(
    modifier: Modifier = Modifier,
    months: YearMonth = YearMonth.now(),
    selectedDate: LocalDate,
    today: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit = {}
) {

    val daysInMonth = months.lengthOfMonth()
    val datesInMonth = (1..daysInMonth).map { day -> months.atDay(day) }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        val index = datesInMonth.indexOf(selectedDate)
        if (index >= 0) {
            listState.scrollToItem(index)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Today: " + today.format(DateTimeFormatter.ofPattern("d,MMM", Locale.getDefault())),
            style = BalooTypography.displayMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        LazyRow (
            modifier = Modifier,
            state = listState,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
        ) {
            items (datesInMonth) { date ->
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val isSelected = date == selectedDate

                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .width(55.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    )

                    Button(
                        onClick = {
                            onDateSelected(date)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) {
                                Color(0xFFCFFFD6)
                            } else {
                                if (isSystemInDarkTheme()) Color.LightGray else Color(0xFFDBDBDB)
                            },
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Gray
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = if (isSelected) 0.dp else 6.dp,
                            pressedElevation = if (isSelected) 0.dp else 4.dp,
                            hoveredElevation = if (isSelected) 0.dp else 4.dp,
                            focusedElevation = if (isSelected) 0.dp else 4.dp
                        ),
                        modifier = Modifier
                            .height(70.dp)
                            .padding(horizontal = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .then(
                                    if (isSelected) Modifier
                                        .background(Color(0xFFACF0E2), CircleShape)
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 16.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }


}