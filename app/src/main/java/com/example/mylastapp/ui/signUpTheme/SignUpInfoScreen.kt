package com.example.mylastapp.ui.signUpTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.R

@Composable
fun SignUpInfoScreen(
    modifier: Modifier = Modifier,
    existPatient: Boolean?,
    onChangeExistPatient: (Boolean?) -> Unit,
    ic: String,
    onChangeIc: (String) -> Unit,
    name: String,
    onChangeName: (String) -> Unit,
    email: String,
    onChangeEmail: (String) -> Unit,
    phone: String,
    onChangePhone: (String) -> Unit,
    read: Boolean,
    onChangeRead: (Boolean) -> Unit,
    errorMessage: String?,
    onNextClick: () -> Unit
) {
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPhone = remember { FocusRequester() }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(if(isSystemInDarkTheme()) Color.DarkGray else Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "About Me",
                color = if(isSystemInDarkTheme()) Color.LightGray else Color.Black
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 15.dp)
        ) {

            Text(
                text = stringResource(R.string.ic_passport),
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditIcTextField(
                ic = ic,
                onChangeIc = onChangeIc,
                errorMessage = errorMessage,
                onNext = { focusRequesterName.requestFocus() }
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Text(
                text = "Full Name",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditNameTextField(
                name = name,
                onChangeName = onChangeName,
                onNext = { focusRequesterEmail.requestFocus() }
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Text(
                text = "Email",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditEmailTextField(
                email = email,
                onChangeEmail = onChangeEmail,
                errorMessage = errorMessage,
                onNext = { focusRequesterPhone.requestFocus() }
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Text(
                text = "Phone Number",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditPhoneTextField(
                phone = phone,
                onChangePhone = onChangePhone,
                errorMessage = errorMessage
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Text(
                text = "Are you our existing patient?",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditExistPatient(
                existPatient = existPatient,
                onChangeExistPatient = onChangeExistPatient
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Text(
                text = "I have read and agree to the Terms & Conditions",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
            )

            EditRead(
                read = read,
                onChangeRead = onChangeRead
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Terms & Conditions",
                    color = if (isSystemInDarkTheme()) Color(0xFF6F97F9) else Color.Blue,
                    fontSize = 13.sp

                )

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                NextButton(
                    existPatient = existPatient,
                    ic = ic,
                    name = name,
                    email = email,
                    phone = phone,
                    read = read,
                    onClick = onNextClick
                )
            }


        }

    }
}

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    existPatient: Boolean?,
    ic: String,
    name: String,
    email: String,
    phone: String,
    read: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(200.dp),
        enabled = existPatient != null && ic.isNotBlank() && name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && read,
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

@Composable
fun EditRead(
    modifier: Modifier = Modifier,
    read: Boolean,
    onChangeRead: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = read,
            onClick = { onChangeRead(true) }
        )
        Text(
            text = "Yes",
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Composable
fun EditPhoneTextField(
    modifier: Modifier = Modifier,
    phone: String,
    errorMessage: String?,
    onChangePhone: (String) -> Unit
) {
    TextField(
        value = phone,
        onValueChange = onChangePhone,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        singleLine = true,
        isError = errorMessage?.contains("Phone") == true,
        supportingText = {
            if (errorMessage?.contains("Phone") == true) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun EditEmailTextField(
    modifier: Modifier = Modifier,
    email: String,
    errorMessage: String?,
    onChangeEmail: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit
) {
    TextField(
        value = email,
        onValueChange = onChangeEmail,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        singleLine = true,
        isError = errorMessage?.contains("Email") == true,
        supportingText = {
            if (errorMessage?.contains("Email") == true) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext
        )
    )
}


@Composable
fun EditNameTextField(
    modifier: Modifier = Modifier,
    name: String,
    onChangeName: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit
) {
    TextField(
        value = name,
        onValueChange = onChangeName,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext
        )
    )
}

@Composable
fun EditIcTextField(
    modifier: Modifier = Modifier,
    ic: String,
    errorMessage: String?,
    onChangeIc: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit
) {
    TextField(
        value = ic,
        onValueChange = onChangeIc,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        singleLine = true,
        isError = errorMessage?.contains("IC") == true,
        supportingText = {
            if (errorMessage?.contains("IC") == true) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext
        )
    )
}

@Composable
fun EditExistPatient(
    modifier: Modifier = Modifier,
    existPatient: Boolean?,
    onChangeExistPatient: (Boolean?) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        RadioButton(
            selected = (existPatient == true),
            onClick = { onChangeExistPatient(true) }
        )
        Text(
            text = "Yes",
            color = if (isSystemInDarkTheme()) Color.White else Color.Black

        )

        Spacer(
            modifier = Modifier
                .width(20.dp)
        )

        RadioButton(
            selected = (existPatient == false),
            onClick = {onChangeExistPatient(false)}
        )
        Text(
            text = "No",
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}