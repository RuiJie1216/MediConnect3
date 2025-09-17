package com.example.mylastapp.ui.userTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.AppScreen

@Composable
fun UserChooseBar(
    modifier: Modifier = Modifier,
    chooseBar: AppScreen,
    onTurnClick: (AppScreen) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .size(90.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .background(Color(0xFFB3EFE9), RoundedCornerShape(40.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(40.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { if (chooseBar != AppScreen.UserAppointment) onTurnClick(AppScreen.UserAppointment) }.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Appointment",
                    modifier = Modifier.size(28.dp),
                    tint = if (chooseBar == AppScreen.UserAppointment) Color(0xFF00C8B3) else Color.Gray
                )
                Text(
                    text = "Appointment",
                    fontSize = 12.sp,
                    color = if (chooseBar == AppScreen.UserAppointment) Color(0xFF00C8B3) else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { if(chooseBar != AppScreen.UserHome) onTurnClick(AppScreen.UserHome) }.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(28.dp),
                    tint = if (chooseBar == AppScreen.UserHome) Color(0xFF00C8B3) else Color.Gray
                )
                Text(
                    text = "Home",
                    fontSize = 12.sp,
                    color = if (chooseBar == AppScreen.UserHome) Color(0xFF00C8B3) else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { if (chooseBar != AppScreen.UserProfile) onTurnClick(AppScreen.UserProfile) }.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(28.dp),
                    tint = if (chooseBar == AppScreen.UserProfile) Color(0xFF00C8B3) else Color.Gray
                )
                Text(
                    text = "Profile",
                    fontSize = 12.sp,
                    color = if (chooseBar == AppScreen.UserProfile) Color(0xFF00C8B3) else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}