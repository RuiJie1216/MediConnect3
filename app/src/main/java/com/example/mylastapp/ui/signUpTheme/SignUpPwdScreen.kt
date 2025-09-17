package com.example.mylastapp.ui.signUpTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylastapp.ui.theme.ArimaTypography
import com.example.mylastapp.ui.theme.MyLastAppTheme

@Composable
fun SignUpPwdScreen(
    modifier: Modifier = Modifier,
    newPwd: String,
    onChangeNewPwd: (String) -> Unit,
    confirmPwd: String,
    onChangeConfirmPwd: (String) -> Unit,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    errorMessage: String?
) {
    val focusRequestCfmPwd = remember { FocusRequester() }

    var calcProgress by remember(newPwd) { mutableFloatStateOf(0f) }

    val conditionCheck = listOf(
        newPwd.length >= 6,
        newPwd.any { it.isUpperCase() },
        newPwd.any { it.isLowerCase() },
        newPwd.any { it.isDigit() }
    )

    calcProgress = conditionCheck.count{ it } / 4f
    val safeProgress = calcProgress.coerceIn(0f, 1f)

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(if(isSystemInDarkTheme()) Color.DarkGray else Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set Password",
                color = if(isSystemInDarkTheme()) Color.LightGray else Color.Black
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 15.dp)
        ) {
            Text(
                text = "Password",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditNewPwdTextField(
                newPwd = newPwd,
                onChangePwd = onChangeNewPwd,
                onNext = { focusRequestCfmPwd.requestFocus() }
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            CheckPwdFormat(
                safeProgress = safeProgress,
                conditionCheck = conditionCheck,
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
            )

            Text(
                text = "Confirm Password",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditConfirmPwdTextField(
                confirmPwd = confirmPwd,
                onChangeConfirmPwd = onChangeConfirmPwd,
                errorMessage = errorMessage
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            if (errorMessage != "Passwords do not match" && errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = ArimaTypography.displaySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBackClick,
                    modifier = Modifier
                        .width(100.dp)
                ) {
                    Text(
                        text = "Back"
                    )
                }

                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .width(100.dp),
                    enabled = newPwd.isNotBlank() && confirmPwd.isNotBlank() && safeProgress > 0.75f,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color(0xFF089A2D) else Color(0xFF34C759),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Confirm"
                    )
                }
            }


        }
    }

}

@Composable
fun EditConfirmPwdTextField(
    modifier: Modifier = Modifier,
    confirmPwd: String,
    errorMessage: String?,
    onChangeConfirmPwd: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = confirmPwd,
        onValueChange = onChangeConfirmPwd,
        singleLine = true,
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(8.dp),
        isError = errorMessage?.contains("Passwords do not match") == true,
        supportingText = {
            if (errorMessage?.contains("Passwords do not match") == true) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(
                onClick = { passwordVisible = !passwordVisible }
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = description
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun CheckPwdFormat(
    modifier: Modifier = Modifier,
    safeProgress: Float,
    conditionCheck: List<Boolean>
) {

    val color = when {
        safeProgress < 0.5f -> Color.Red
        safeProgress < 0.75f -> Color(0xFFFFA500)
        safeProgress < 1f -> Color.Yellow
        else -> Color.Green
    }

    Column(
        modifier = modifier
    ) {
        LinearProgressIndicator(
            progress = { safeProgress },
            color = color,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            modifier = Modifier
                .padding(vertical = 5.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "check 6 character",
                tint = if (!conditionCheck[0]) {
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                } else {
                    Color.Green
                }
            )
            Text(
                text = "At least 6 characters",
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "check one upper",
                tint = if (!conditionCheck[1]) {
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                } else {
                    Color.Green
                }
            )
            Text(
                text = "At least one capital letter",
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "check one lower",
                tint = if (!conditionCheck[2]) {
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                } else {
                    Color.Green
                }
            )
            Text(
                text = "At least one small letter",
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "check one digit",
                tint = if (!conditionCheck[3]) {
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                } else {
                    Color.Green
                }
            )
            Text(
                text = "At least one digit",
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }

    }
}

@Composable
fun EditNewPwdTextField(
    newPwd: String,
    onChangePwd: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = newPwd,
        onValueChange = onChangePwd,
        singleLine = true,
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpPwdScreenPreview() {
    MyLastAppTheme {
        SignUpPwdScreen(
            newPwd = "",
            onChangeNewPwd = {},
            confirmPwd = "",
            onChangeConfirmPwd = {},
            onBackClick = {},
            onConfirmClick = {},
            errorMessage = null
        )
    }
}