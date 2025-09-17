package com.example.mylastapp.ui.loginTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylastapp.AppScreen
import com.example.mylastapp.R
import com.example.mylastapp.ui.theme.ArimaTypography
import com.example.mylastapp.ui.theme.BalooTypography
import com.example.mylastapp.ui.theme.MyLastAppTheme

@Composable
fun DoctorLoginScreen(
    modifier: Modifier = Modifier,
    onForgetPwdClick: () -> Unit,
    onLoginClick: () -> Unit,
    onTurnUsersClick: () -> Unit,
    chooseBar: AppScreen,
    errorMessage: String?,
    id: String,
    onChangeId: (String) -> Unit,
    pwd: String,
    onChangePwd: (String) -> Unit
) {
    val onFocusRequestPwd = remember { FocusRequester() }
    var pwdVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.loginpage),
            contentDescription = "LoginPageBackground",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = BalooTypography.titleMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(1f))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x6D00C8B3)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.welcome),
                        style = ArimaTypography.displayLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 30.dp, top = 50.dp)
                    )

                    EditDocIdTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 45.dp)
                            .height(65.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(35.dp)
                            ),
                        value = id,
                        errorMessage = errorMessage,
                        onChangeValue = onChangeId,
                        onNext = { onFocusRequestPwd.requestFocus() }
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    EditDocPwdTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 45.dp)
                            .height(65.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(35.dp)
                            ),
                        value = pwd,
                        errorMessage = errorMessage,
                        onChangeValue = onChangePwd,
                        onClick = { pwdVisible = !pwdVisible },
                        pwdVisible = pwdVisible
                    )

                    ForgotDocPwdButton(onClick = onForgetPwdClick)

                    LoginDocButton(
                        onClick = onLoginClick,
                        id = id,
                        pwd = pwd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 70.dp)
                            .height(40.dp)
                    )

                    Spacer(modifier = Modifier.height(44.dp))
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            LoginChooseButtonBar(
                modifier = Modifier.padding(bottom = 20.dp),
                chooseBar = chooseBar,
                onTurnPageClick = onTurnUsersClick
            )

            Spacer(modifier = Modifier.weight(0.5f))

        }
    }
}


@Composable
fun LoginDocButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    id: String,
    pwd: String
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = id.isNotEmpty() && pwd.isNotEmpty() && pwd.length >= 6,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContainerColor = Color.Black.copy(alpha = 0.2f),
            disabledContentColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = stringResource(R.string.login),
            style = ArimaTypography.displayMedium
        )
    }

}

@Composable
fun ForgotDocPwdButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.forgot_pwd),
            style = ArimaTypography.displaySmall,
            color = Color.Blue
        )
    }

}

@Composable
fun EditDocIdTextField(
    modifier: Modifier = Modifier,
    value: String,
    errorMessage: String?,
    onChangeValue: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit
) {

    Box(
        if (errorMessage?.contains("ID") == true) {
            modifier.border(width = 2.dp,
                color = Color(0xFFB3261E),
                shape = RoundedCornerShape(35.dp)
            )
        } else modifier

    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.email),
                contentDescription = "Email Picture",
                modifier = Modifier
                    .size(30.dp),
            )

            TextField(
                placeholder = {
                    if (errorMessage?.contains("ID") == true) {
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("ID", color = Color.Gray)
                    }
                },
                value = value,
                onValueChange = onChangeValue,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    errorCursorColor = Color(0xFFB3261E),
                    errorPlaceholderColor = Color(0xFFB3261E)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = onNext
                ),
                modifier = Modifier
                    .padding(5.dp)
            )

        }
    }
}

@Composable
fun EditDocPwdTextField(
    modifier: Modifier = Modifier,
    value: String,
    errorMessage: String?,
    onChangeValue: (String) -> Unit,
    onClick: () -> Unit,
    pwdVisible: Boolean
) {
    Box(
        if (errorMessage?.contains("Error") == true) {
            modifier.border(width = 2.dp,
                color = Color(0xFFB3261E),
                shape = RoundedCornerShape(35.dp)
            )
        } else modifier

    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.password),
                contentDescription = "Email Picture",
                modifier = Modifier
                    .size(30.dp),
            )

            TextField(
                placeholder = {
                    if (errorMessage?.contains("Error") == true) {
                        Text("Wrong Password", color = MaterialTheme.colorScheme.error, fontSize = 10.sp)
                    } else {
                        Text("Password", color = Color.Gray)
                    }
                },
                value = value,
                onValueChange = onChangeValue,
                singleLine = true,
                visualTransformation = if (pwdVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    errorCursorColor = Color(0xFFB3261E),
                    errorPlaceholderColor = Color(0xFFB3261E)

                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .heightIn(min = 64.dp)



            )

            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(25.dp)
            ) {
                Icon(
                    imageVector = if(pwdVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (pwdVisible) "Hide password" else "Show password",
                    tint = Color.Gray
                )
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DocLoginScreenPreview() {
    MyLastAppTheme {
        DoctorLoginScreen(
            modifier = Modifier
                .fillMaxHeight(),
            id = "",
            pwd = "",
            chooseBar = AppScreen.DocLogin,
            errorMessage = null,
            onChangeId = {},
            onChangePwd = {},
            onForgetPwdClick = {},
            onTurnUsersClick = {},
            onLoginClick = {}


        )
    }
}