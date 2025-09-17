package com.example.mylastapp.ui

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mylastapp.ui.viewModel.ForgotPwdViewModel


enum class ForWho{
    Users,
    Doctors
}

@Composable
fun ForgotPwdScreen(
    modifier: Modifier = Modifier,
    forWho: ForWho,
    icID: String,
    onChangeIcID: (String) -> Unit,
    isSending: Boolean,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Text(
                text = if (forWho == ForWho.Users) "Please enter your IC/Passport" else "Please enter your ID",
                fontSize = 12.sp
            )
        }
        OutlinedTextField(
            value = icID,
            onValueChange = onChangeIcID,
            label = { Text(if(forWho == ForWho.Users) "IC/Passport" else "ID") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        if (icID.isNotBlank() && if (forWho == ForWho.Users) !icID.matches(Regex("\\d{12}")) else !icID.matches(Regex("\\d"))) {
            Text(
                text = if (forWho == ForWho.Users) "* Invalid IC/Passport" else "* Invalid ID",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSendClick,
            enabled = !isSending && icID.isNotBlank() &&
                    if (forWho == ForWho.Users) icID.matches(Regex("\\d{12}")) else icID.matches(Regex("\\d")),
            modifier = Modifier
                .width(200.dp)
        ) {
            Text(if (isSending) "Sending..." else "Send Reset Email")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onBackClick,
        ) {
            Text("Back to Login")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ForgotPwdScreenPreview() {
    ForgotPwdScreen(
        modifier = Modifier
            .fillMaxSize(),
        icID = "",
        onChangeIcID = {},
        isSending = false,
        onSendClick = {},
        onBackClick = {},
        forWho = ForWho.Users
    )
}