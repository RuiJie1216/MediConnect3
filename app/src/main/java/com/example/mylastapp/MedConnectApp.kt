package com.example.mylastapp

import android.R
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylastapp.ui.rooms.repo.UsersRepo
import com.example.mylastapp.ui.viewModel.UsersViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mylastapp.ui.theme.BalooTypography
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.navigation.compose.navigation
import com.example.mylastapp.ui.ForWho
import com.example.mylastapp.ui.ForgotPwdScreen
import com.example.mylastapp.ui.doctorTheme.DocAppointmentScreen
import com.example.mylastapp.ui.doctorTheme.DocCheckInScreen
import com.example.mylastapp.ui.doctorTheme.DocEditPatientScreen
import com.example.mylastapp.ui.doctorTheme.DocEditProfileScreen
import com.example.mylastapp.ui.doctorTheme.DocHomeScreen
import com.example.mylastapp.ui.doctorTheme.DocPatientInfoScreen
import com.example.mylastapp.ui.doctorTheme.DocPatientsScreen
import com.example.mylastapp.ui.doctorTheme.DocProfileScreen
import com.example.mylastapp.ui.doctorTheme.MedicationUiState
import com.example.mylastapp.ui.loginTheme.DoctorLoginScreen
import com.example.mylastapp.ui.loginTheme.UserLoginScreen
import com.example.mylastapp.ui.rooms.entity.Appointments
import com.example.mylastapp.ui.rooms.entity.MedicalReminder
import com.example.mylastapp.ui.rooms.entity.Patients
import com.example.mylastapp.ui.rooms.repo.AppointmentsRepo
import com.example.mylastapp.ui.rooms.repo.DoctorsRepo
import com.example.mylastapp.ui.rooms.repo.MedicalRepo
import com.example.mylastapp.ui.rooms.repo.PatientsRepo
import com.example.mylastapp.ui.signUpTheme.SignUpInfoScreen
import com.example.mylastapp.ui.signUpTheme.SignUpPwdScreen
import com.example.mylastapp.ui.signUpTheme.SignUpSuccessScreen
import com.example.mylastapp.ui.userTheme.UserAppointmentScreen
import com.example.mylastapp.ui.userTheme.UserConfirmBookScreen
import com.example.mylastapp.ui.userTheme.UserHomeScreen
import com.example.mylastapp.ui.userTheme.UserMedicReminderScreen
import com.example.mylastapp.ui.userTheme.UserPersonalInfoScreen
import com.example.mylastapp.ui.userTheme.UserProfileScreen
import com.example.mylastapp.ui.userTheme.UserSettingScreen
import com.example.mylastapp.ui.userTheme.UserSuccessBookScreen
import com.example.mylastapp.ui.userTheme.UserViewAppointmentScreen
import com.example.mylastapp.ui.userTheme.ViewState
import com.example.mylastapp.ui.userTheme.specialties
import com.example.mylastapp.ui.viewModel.AppointmentsViewModel
import com.example.mylastapp.ui.viewModel.DoctorsViewModel
import com.example.mylastapp.ui.viewModel.ForgotPwdViewModel
import com.example.mylastapp.ui.viewModel.SignUpViewModel
import java.time.DayOfWeek
import java.time.LocalTime
import kotlin.text.contains


class AppointmentsViewModelFactory(private val appointmentsRepo: AppointmentsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppointmentsViewModel(appointmentsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class UsersViewModelFactory(private val userRepo: UsersRepo, private val medicalRepo: MedicalRepo, private val docRepo: DoctorsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(userRepo, medicalRepo, docRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DoctorsViewModelFactory(private val docRepo: DoctorsRepo, private val patientsRepo: PatientsRepo, private val medicalRepo: MedicalRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoctorsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DoctorsViewModel(docRepo, patientsRepo, medicalRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

enum class AppScreen {
    //System
    LoginSystem,
    UserSystem,
    DocSystem,
    SignUpSystem,

    //ForgotPassword
    ForgotPassword,

    //SignUp
    SignUpInfo,
    SignUpPwd,
    SignUpSuccess,

    //Login
    UserLogin,
    DocLogin,

    //User
    UserHome,
    UserAppointment,
    UserConfirmBook,
    UserSuccessBook,
    UserProfile,
    UserMedicalReminder,
    UserPersonalInfo,
    UserViewAppointment,
    UserSetting,

    //Doctor
    DocHome,
    DocPatient,
    DocPatientInfo,
    DocEditPatient,
    DocAppointment,
    DocCheckIn,
    DocProfile,
    DocEditProfile,

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreen(
    currentScreen: AppScreen,
    searchQuery: String,
    onChangeSearchQuery: (String) -> Unit,
    hasNavigate: (AppScreen) -> Unit,
    hasPopBack: () -> Unit,
    onDocLogout: () -> Unit
) {
    when(currentScreen) {
        AppScreen.DocHome, AppScreen.UserHome, AppScreen.UserProfile, AppScreen.UserMedicalReminder, AppScreen.UserPersonalInfo -> {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "MEDICONNECT",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.DocProfile -> {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(
                        onClick = { hasNavigate(AppScreen.DocHome) }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { hasNavigate(AppScreen.DocEditProfile) }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = onDocLogout
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    actionIconContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            )
        }
        AppScreen.DocEditProfile -> {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(
                        onClick = { hasNavigate(AppScreen.DocProfile) }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    actionIconContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            )
        }
        AppScreen.DocPatient -> {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = onChangeSearchQuery,
                        placeholder = { Text("Search patients...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF29E6D2),
                            unfocusedContainerColor = Color(0xFF29E6D2),
                            disabledContainerColor = Color(0xFF29E6D2),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { hasNavigate(AppScreen.DocHome) }
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF29E6D2)
                )
            )
        }
        AppScreen.DocPatientInfo -> {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { hasNavigate(AppScreen.DocPatient) }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .padding(end = 8.dp)

                            )
                        }

                        Text(
                            text = "PATIENT INFO",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        IconButton(
                            onClick = { hasNavigate(AppScreen.DocEditPatient) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.EditNote,
                                contentDescription = "Edit",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.DocEditPatient -> {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "EDIT PATIENT INFO",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        IconButton(
                            onClick = { hasNavigate(AppScreen.DocPatientInfo) },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.DocAppointment -> {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Appointment List",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        IconButton(
                            onClick = { hasNavigate(AppScreen.DocHome) },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.DocCheckIn -> {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Consultation Records",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        IconButton(
                            onClick = { hasNavigate(AppScreen.DocAppointment) },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.UserAppointment -> {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Appointment",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.UserConfirmBook -> {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Confirm Book",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = hasPopBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.UserSetting, AppScreen.UserViewAppointment -> {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "MEDICONNECT",
                            style = BalooTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = hasPopBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                )
            )
        }
        AppScreen.SignUpInfo, AppScreen.SignUpPwd -> {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign Up"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C8B3),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { hasNavigate(AppScreen.LoginSystem) }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
        else -> {}
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedConnectApp(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = viewModel(),
    forgotPwdViewModel: ForgotPwdViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current


    //App Database
    val db = remember { AppDatabase.getInstance(context) }

    //Repository
    val usersRepo = remember { UsersRepo(db.usersDao()) }
    val doctorsRepo = remember { DoctorsRepo(db.doctorsDao()) }
    val patientsRepo = remember { PatientsRepo(db.patientsDao()) }
    val medicalRepo = remember { MedicalRepo(db.medicalDao()) }
    val appointmentsRepo = remember { AppointmentsRepo(db.appointmentsDao()) }

    //Factory
    val usersViewModelFactory = remember { UsersViewModelFactory(usersRepo, medicalRepo, doctorsRepo) }
    val doctorsViewModelFactory = remember { DoctorsViewModelFactory(doctorsRepo, patientsRepo, medicalRepo) }
    val appointmentsViewModelFactory = remember { AppointmentsViewModelFactory(appointmentsRepo) }

    //ViewModel
    val usersViewModel: UsersViewModel = viewModel(
        factory = usersViewModelFactory
    )

    val doctorsViewModel: DoctorsViewModel = viewModel(
        factory = doctorsViewModelFactory
    )

    val appointmentsViewModel: AppointmentsViewModel = viewModel(
        factory = appointmentsViewModelFactory
    )

    //Doctor
    val doctors by doctorsViewModel.doctors.collectAsState()
    val currentDoctor by doctorsViewModel.currentDoctor.collectAsState()
    val patients by doctorsViewModel.patients.collectAsState()
    val currentPatient by doctorsViewModel.currentPatient.collectAsState()
    val appointments by appointmentsViewModel.appointments.collectAsState()
    val currentAppointment by appointmentsViewModel.currentAppointment.collectAsState()

    //User
    val users by usersViewModel.users.collectAsState()
    val currentUser by usersViewModel.currentUser.collectAsState()
    val allMedicalReminders by usersViewModel.allMedicalReminders.collectAsState()
    val selectedDoctor by usersViewModel.selectedDoctor.collectAsState()


    LaunchedEffect(Unit) {
        appointmentsViewModel.syncAppointmentsFirebase()
    }


    val signUpUiState by signUpViewModel.signUpUiState.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.LoginSystem.name
    )

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var docLogOutConfirm by rememberSaveable { mutableStateOf(false) }
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var forWho by remember{ mutableStateOf(ForWho.Users) }

    Scaffold(
        topBar = {
            TopBarScreen(
                currentScreen = currentScreen,
                searchQuery = searchQuery,
                onChangeSearchQuery = { searchQuery = it },
                hasNavigate = {navController.navigate(it.name)},
                onDocLogout = {docLogOutConfirm = true},
                hasPopBack = {navController.popBackStack()}
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppScreen.LoginSystem.name,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            //LoginPage
            navigation(
                startDestination = AppScreen.UserLogin.name,
                route = AppScreen.LoginSystem.name
            ) {
                //UserLogin
                composable(route = AppScreen.UserLogin.name) {
                    var ic by rememberSaveable { mutableStateOf("") }
                    var pwd by rememberSaveable { mutableStateOf("") }
                    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

                    UserLoginScreen(
                        modifier = Modifier
                            .fillMaxHeight(),
                        chooseBar = currentScreen,
                        errorMessage = errorMessage,
                        ic = ic,
                        pwd = pwd,
                        onChangeIc = {
                            ic = it
                            if (errorMessage?.contains("IC") == true) {
                                errorMessage = null
                            }
                        },
                        onchangePwd = {
                            pwd = it
                            if (errorMessage?.contains("Error") == true) {
                                errorMessage = null
                            }
                        },
                        onLoginClick = {
                            errorMessage = null
                            usersViewModel.login(ic, pwd, context) { success, message ->
                                if (success) {
                                    navController.navigate(AppScreen.UserSystem.name)
                                } else {
                                    errorMessage = message
                                    if (errorMessage?.contains("IC") == true) {
                                        ic = ""
                                        pwd = ""
                                    }
                                    else {
                                        pwd = ""
                                    }
                                }
                            }
                        },
                        onTurnDoctorClick = {
                            navController.navigate(AppScreen.DocLogin.name)
                        },
                        onSignUpClick = {
                            signUpViewModel.reset()
                            navController.navigate(AppScreen.SignUpSystem.name)
                        },
                        onForgetPwdClick = {
                            forWho = ForWho.Users
                            navController.navigate(AppScreen.ForgotPassword.name)
                        }
                    )
                }

                //DoctorLogin
                composable(route = AppScreen.DocLogin.name) {
                    var id by rememberSaveable { mutableStateOf("") }
                    var pwd by rememberSaveable { mutableStateOf("") }
                    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

                    DoctorLoginScreen(
                        modifier = Modifier
                            .fillMaxHeight(),
                        id = id,
                        pwd = pwd,
                        chooseBar = currentScreen,
                        errorMessage = errorMessage,
                        onChangeId = {
                            id = it
                            if (errorMessage?.contains("ID") == true) {
                                errorMessage = null
                            }
                        },
                        onChangePwd = {
                            pwd = it
                            if (errorMessage?.contains("Error") == true) {
                                errorMessage = null
                            }
                        },
                        onForgetPwdClick = {
                            forWho = ForWho.Doctors
                            navController.navigate(AppScreen.ForgotPassword.name)
                        },
                        onTurnUsersClick = {
                            navController.navigate(AppScreen.UserLogin.name)
                        },
                        onLoginClick = {
                            errorMessage = null
                            doctorsViewModel.login(id, pwd) { success, message ->
                                if (success) {
                                    navController.navigate(AppScreen.DocSystem.name)
                                } else {
                                    errorMessage = message
                                    if (errorMessage?.contains("ID") == true) {
                                        id = ""
                                        pwd = ""
                                    }
                                    else {
                                        pwd = ""
                                    }
                                }
                            }
                        }


                    )
                }
            }

            composable(route = AppScreen.ForgotPassword.name) {
                var icID by remember { mutableStateOf("") }
                var isSending by remember { mutableStateOf(false) }

                ForgotPwdScreen(
                    modifier = modifier
                        .fillMaxHeight(),
                    forWho = forWho,
                    icID = icID,
                    onChangeIcID = {icID = it},
                    isSending = isSending,
                    onSendClick = {
                        if (forWho == ForWho.Users) {
                            usersViewModel.getUserByIc(icID) { user ->
                                if (user != null) {
                                    forgotPwdViewModel.sendPasswordResetEmail(user.email) { success, message ->
                                        isSending = false
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                        if (success) {
                                            navController.popBackStack()
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(context, "IC not found", Toast.LENGTH_LONG).show()
                                }
                            }

                        } else {
                            doctorsViewModel.getDoctorById(icID) { doctor ->
                                if (doctor != null) {
                                    isSending = true
                                    forgotPwdViewModel.sendPasswordResetEmail(doctor.email) { success, message ->
                                        isSending = false
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                        if (success) {
                                            navController.popBackStack()
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(context, "Doctor ID not found", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            //Sign Up
            navigation(
                startDestination = AppScreen.SignUpInfo.name,
                route = AppScreen.SignUpSystem.name
            ) {
                composable(route = AppScreen.SignUpInfo.name) {
                    var existPatient by rememberSaveable { mutableStateOf<Boolean?>(signUpUiState.existPatient) }
                    var ic by rememberSaveable { mutableStateOf(signUpUiState.ic) }
                    var name by rememberSaveable { mutableStateOf(signUpUiState.name) }
                    var email by rememberSaveable { mutableStateOf(signUpUiState.email) }
                    var phone by rememberSaveable { mutableStateOf(signUpUiState.phone) }
                    var read by rememberSaveable { mutableStateOf(signUpUiState.read) }
                    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

                    val icRegex = Regex("^\\d{12}$")
                    val passportRegex = Regex("^[A-PR-WYa-pr-wy][1-9]\\d{5,8}$")
                    val phoneRegex = Regex("^01[0-9]{8,9}$")


                    SignUpInfoScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        existPatient = existPatient,
                        onChangeExistPatient = {existPatient = it},
                        ic = ic,
                        onChangeIc = {
                            ic = it
                            if (errorMessage?.contains("IC") == true) {
                                errorMessage = null
                            }
                        },
                        name = name,
                        onChangeName = {name = it},
                        email = email,
                        onChangeEmail = {
                            email = it
                            if (errorMessage?.contains("Email") == true) {
                                errorMessage = null
                            }
                        },
                        phone = phone,
                        onChangePhone = {
                            phone = it
                            if (errorMessage?.contains("Phone") == true) {
                                errorMessage = null
                            }
                        },
                        read = read,
                        onChangeRead = {read = it},
                        errorMessage = errorMessage,
                        onNextClick = {
                            if (!ic.matches(icRegex) && !ic.matches(passportRegex)) {
                                errorMessage = "Invalid IC"
                            }
                            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                errorMessage = "Invalid Email"
                            }
                            else if (!phone.matches(phoneRegex)) {
                                errorMessage = "Invalid Phone"
                            }
                            else {
                                usersViewModel.checkUser(ic, email, phone) { success, message ->
                                    if (success) {
                                        signUpViewModel.setExistPatient(existPatient?: false)
                                        signUpViewModel.setIc(ic)
                                        signUpViewModel.setName(name)
                                        signUpViewModel.setEmail(email)
                                        signUpViewModel.setPhone(phone)
                                        signUpViewModel.setRead(read)

                                        navController.navigate(AppScreen.SignUpPwd.name)
                                    } else {
                                        errorMessage = message
                                    }
                                }
                            }
                        }
                    )
                }

                composable(route = AppScreen.SignUpPwd.name) {
                    var newPwd by rememberSaveable { mutableStateOf("") }
                    var confirmPwd by rememberSaveable { mutableStateOf("") }
                    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }


                    SignUpPwdScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        newPwd = newPwd,
                        onChangeNewPwd = {newPwd = it},
                        confirmPwd = confirmPwd,
                        onChangeConfirmPwd = {
                            confirmPwd = it
                            if (errorMessage?.contains("Passwords do not match") == true) {
                                errorMessage = null
                            }
                        },
                        errorMessage = errorMessage,
                        onBackClick = {
                            navController.navigate(AppScreen.SignUpInfo.name)
                        },
                        onConfirmClick = {
                            errorMessage = null
                            if (newPwd != confirmPwd) {
                                confirmPwd = ""
                                errorMessage = "Passwords do not match"
                            }
                            else {
                                signUpViewModel.setPwd(newPwd)
                                val updateState = signUpUiState.copy(pwd = newPwd)
                                usersViewModel.signup(updateState) { success, message ->
                                    if (success) {
                                        navController.navigate(AppScreen.SignUpSuccess.name)
                                    } else {
                                        errorMessage = message
                                    }
                                }
                            }

                        }
                    )
                }

                composable(route = AppScreen.SignUpSuccess.name) {
                    SignUpSuccessScreen(
                        modifier = modifier
                            .fillMaxSize(),
                        onBackClick = { navController.navigate(AppScreen.LoginSystem.name) }
                    )
                }
            }

            //DoctorPage
            navigation(
                startDestination = AppScreen.DocHome.name,
                route = AppScreen.DocSystem.name
            ){
                //Home
                composable(route = AppScreen.DocHome.name) {
                    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    LaunchedEffect(Unit) {
                        appointmentsViewModel.getAppointmentsByDoctorIDAndDate(currentDoctor?.id ?: "", today)
                    }
                    DocHomeScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        onAppointmentClick = {
                            selectedDate = LocalDate.now()
                            navController.navigate(AppScreen.DocAppointment.name)
                        },
                        onProfileClick = {
                            navController.navigate(AppScreen.DocProfile.name)
                        },
                        onPatientClick = {
                            searchQuery = ""
                            navController.navigate(AppScreen.DocPatient.name)
                        },
                        appointments = appointments
                    )
                }

                //PatientInfo
                composable(route = AppScreen.DocPatient.name) {
                    doctorsViewModel.getPatientsByDoctorID(currentDoctor?.id ?: "")
                    DocPatientsScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        searchQuery = searchQuery,
                        onAddClick = {},
                        onPatientDetailClick = {
                            doctorsViewModel.setCurrentPatient(it)
                            navController.navigate(AppScreen.DocPatientInfo.name)
                        },
                        onBackClick = {
                            navController.navigate(AppScreen.DocHome.name)
                        },
                        doctorPatients = patients
                    )
                }

                composable(route = AppScreen.DocPatientInfo.name) {
                    DocPatientInfoScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        patient = currentPatient
                    )
                }

                composable(route = AppScreen.DocEditPatient.name) {
                    var id by rememberSaveable { mutableStateOf(currentPatient?.patient?.patientID ?: 0) }
                    var complaint by rememberSaveable { mutableStateOf(currentPatient?.patient?.complaint ?: "") }
                    var hpi by rememberSaveable { mutableStateOf(currentPatient?.patient?.hpi ?: "") }
                    var medicationHistory by rememberSaveable { mutableStateOf(currentPatient?.patient?.medicationHistory ?: "") }
                    var followUp by rememberSaveable { mutableStateOf(currentPatient?.patient?.followUp ?: "") }

                    DocEditPatientScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        id = id,
                        complaint = complaint,
                        onChangeComplaint = {complaint = it},
                        hpi = hpi,
                        onChangeHpi = {hpi = it},
                        medicationHistory = medicationHistory,
                        onChangeMedicationHistory = {medicationHistory = it},
                        followUp = followUp,
                        onChangeFollowUp = {followUp = it},
                        onConfirm = {
                            val updatedPatient = Patients(
                                patientID = currentPatient?.patient?.patientID ?: 0,
                                doctorID = currentPatient?.patient?.doctorID ?: "",
                                patientIC = currentPatient?.patient?.patientIC ?: "",
                                complaint = complaint,
                                hpi = hpi,
                                medicationHistory = medicationHistory,
                                followUp = followUp
                            )

                            doctorsViewModel.updatePatient(updatedPatient)
                            navController.navigate(AppScreen.DocPatient.name)
                        }
                    )
                }

                composable(route = AppScreen.DocProfile.name) {
                    if (docLogOutConfirm) {
                        AlertDialog(
                            onDismissRequest = { docLogOutConfirm = false },
                            title = { Text("Confirm Logout") },
                            text = { Text("Are you sure you want to logout?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    docLogOutConfirm = false
                                    doctorsViewModel.logout()
                                    navController.navigate(AppScreen.LoginSystem.name)
                                }) { Text("Yes") }
                            },
                            dismissButton = {
                                TextButton(onClick = { docLogOutConfirm = false }) { Text("No") }
                            }
                        )
                    }
                    DocProfileScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        doctor = currentDoctor
                    )
                }

                composable(route = AppScreen.DocEditProfile.name) {
                    var name by remember { mutableStateOf(currentDoctor?.name ?: "") }
                    var phone by remember { mutableStateOf(currentDoctor?.phone ?: "") }
                    var degree by remember { mutableStateOf(currentDoctor?.degree ?: "") }
                    var specialty by remember { mutableStateOf(currentDoctor?.specialty ?: "") }
                    var quote by remember { mutableStateOf(currentDoctor?.quote ?: "") }
                    var selectedYear by remember { mutableIntStateOf(currentDoctor?.year ?: 0) }
                    val selectedLanguages = remember { mutableStateOf(currentDoctor?.language?.split(", ")?.filter { it.isNotBlank() } ?: emptyList()) }
                    val selectedDays = remember { mutableStateOf(currentDoctor?.dayOff?.split(", ")?.filter { it.isNotBlank() } ?: emptyList()) }
                    var showSaveDialog by remember { mutableStateOf(false) }

                    DocEditProfileScreen(
                        name = name,
                        onCDocName = {name = it},
                        phone = phone,
                        onCDocPhone = {phone = it},
                        degree = degree,
                        onCDocDegree = {degree = it},
                        specialty = specialty,
                        onCDocSpecialty = {specialty = it},
                        quote = quote,
                        onCDocQuote = {quote = it},
                        year = selectedYear,
                        onCDocYear = {selectedYear = it},
                        language = selectedLanguages.value.joinToString(", "),
                        onCDocLanguage = { newLang -> selectedLanguages.value = newLang },
                        dayOff = selectedDays.value.joinToString(", "),
                        onCDocDayOff = { newDay -> selectedDays.value = newDay },

                        onCamClick = {},
                        onSaveClick = { showSaveDialog = true },
                        onCancelEditClick = {navController.navigate(AppScreen.DocProfile.name)}
                    )
                    if (showSaveDialog) {
                        AlertDialog(
                            onDismissRequest = { showSaveDialog = false },
                            title = { Text("Confirm Save") },
                            text = { Text("Are you sure you want to save the changes?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        val updateDocState = currentDoctor?.copy(
                                            name = name,
                                            phone = phone,
                                            degree = degree,
                                            specialty = specialty,
                                            quote = quote,
                                            year = selectedYear,
                                            language = selectedLanguages.value.joinToString(", "),
                                            dayOff = selectedDays.value.joinToString(", ")
                                        )
                                        updateDocState?.let {
                                            doctorsViewModel.update(it, selectedLanguages.value, selectedDays.value)
                                        }
                                        showSaveDialog = false
                                        navController.navigate(AppScreen.DocProfile.name)
                                    }
                                ) {
                                    Text("Yes")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showSaveDialog = false }) {
                                    Text("No")
                                }
                            }
                        )
                    }
                }

                composable(route = AppScreen.DocAppointment.name) {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    LaunchedEffect(selectedDate) {
                        appointmentsViewModel.getAppointmentsByDoctorIDAndDate(currentDoctor?.id ?: "", selectedDate.format(formatter).toString())
                        Log.d("appointment", "appointment = $appointments")
                    }

                    DocAppointmentScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        docAppointments = appointments,
                        selectedDate = selectedDate,
                        onChangeSelectedDate = {selectedDate = it},
                        onAppointmentClick = {
                            appointmentsViewModel.setCurrentAppointment(it)
                            navController.navigate(AppScreen.DocCheckIn.name)
                        }
                    )
                }

                composable(route = AppScreen.DocCheckIn.name) {
                    var hpi by rememberSaveable { mutableStateOf("") }
                    var followUp by rememberSaveable { mutableStateOf("") }

                    DocCheckInScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        appointment = currentAppointment,
                        hpi = hpi,
                        onChangeHpi = {hpi = it},
                        followUp = followUp,
                        onChangeFollowUp = {followUp = it},
                        onCheckIn = { med ->
                            appointmentsViewModel.updateStatus(currentAppointment?.appointment?.appointmentID ?: 0, "Check-in")
                            doctorsViewModel.updateOrCreatePatient(currentAppointment?.appointment?.userIC ?: "", currentAppointment?.appointment?.complaint ?: "", hpi, med, followUp)

                            med.forEach { medItem ->

                                val dates = calculateMedicationDates(medItem)

                                val reminder = MedicalReminder(
                                    ic = currentAppointment?.appointment?.userIC ?: "",
                                    name = medItem.medicineName,
                                    dose = medItem.dose.toInt(),
                                    instruction = medItem.instruction,
                                    date = dates.joinToString(","),
                                    time = medItem.time,
                                )

                                doctorsViewModel.insertReminder(reminder)
                            }

                            navController.navigate(AppScreen.DocAppointment.name)
                        }

                    )
                }
            }

            //UserPage
            navigation(
                startDestination = AppScreen.UserHome.name,
                route = AppScreen.UserSystem.name
            ){
                //Home
                composable(route = AppScreen.UserHome.name) {
                    LaunchedEffect(Unit) {
                        appointmentsViewModel.getAppointmentByUserIC(currentUser?.ic ?: "")
                    }

                    UserHomeScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        chooseBar = currentScreen,
                        onAppointmentClick = {navController.navigate(AppScreen.UserAppointment.name)},
                        onProfileClick = {navController.navigate(AppScreen.UserProfile.name)},
                        onMedicalReminderClick = {navController.navigate(AppScreen.UserMedicalReminder.name)},
                        appointments = appointments
                    )
                }

                composable(route = AppScreen.UserProfile.name) {
                    var logOutConfirm by rememberSaveable { mutableStateOf(false) }

                    if (logOutConfirm) {
                        AlertDialog(
                            onDismissRequest = { logOutConfirm = false },
                            title = { Text(text = "Confirm Logout") },
                            text = { Text("Are you sure you want to logout?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    logOutConfirm = false
                                    usersViewModel.logout(context)
                                    navController.navigate(AppScreen.LoginSystem.name)
                                }) {
                                    Text("Yes")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { logOutConfirm = false }) {
                                    Text("No")
                                }
                            },
                            properties = DialogProperties(dismissOnClickOutside = false)
                        )
                    }

                    UserProfileScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        currentUser = currentUser,
                        currentScreen = currentScreen,
                        onHomeClick = {navController.navigate(AppScreen.UserHome.name)},
                        onAppointmentClick = {navController.navigate(AppScreen.UserAppointment.name)},
                        onLogoutClick = {
                            logOutConfirm = true
                        },
                        onPersonalInfoClick = {
                            navController.navigate(AppScreen.UserPersonalInfo.name)
                        },
                        onViewAppointmentClick = {
                            navController.navigate(AppScreen.UserViewAppointment.name)
                        },
                        onSettingClick = {
                            navController.navigate(AppScreen.UserSetting.name)
                        }
                    )
                }

                composable(route = AppScreen.UserSetting.name) {
                    UserSettingScreen(
                        modifier = modifier
                            .fillMaxHeight()
                            .background(Color.White)
                    )
                }

                composable(route = AppScreen.UserAppointment.name) {
                    var doctorsList by remember { mutableStateOf(doctors.shuffled()) }
                    var query by remember { mutableStateOf("") }
                    var currentViewState by remember { mutableStateOf(ViewState.SPECIALTIES) }

                    val filteredSpecialties = remember(query) {
                        if (query.isBlank()) {
                            specialties
                        } else {
                            specialties.filter { specialty ->
                                specialty.name.contains(query, ignoreCase = true)
                            }
                        }
                    }

                    val filteredDoctors = remember(query) {
                        if (query.isBlank()) {
                            doctorsList
                        } else {
                            doctorsList.filter { doctor ->
                                doctor.name.contains(query, ignoreCase = true) ||
                                        doctor.specialty.contains(query, ignoreCase = true) ||
                                        doctor.language.split(",").any{ it.contains(query, ignoreCase = true) } }
                        }
                    }

                    UserAppointmentScreen(
                        modifier = Modifier
                            .fillMaxHeight(),
                        filteredSpecialties = filteredSpecialties,
                        filteredDoctors = filteredDoctors,
                        query = query,
                        onChangeQuery = { query = it },
                        currentViewState = currentViewState,
                        onChangeViewState = { currentViewState = it },
                        onSpecialtyCardClick = {
                            query = it.name
                            currentViewState = ViewState.DOCTORS
                                               },
                        onDoctorCardClick = {
                            usersViewModel.setSelectedDoctor(it)
                            navController.navigate(AppScreen.UserConfirmBook.name)
                        },
                        currentScreen = currentScreen,
                        onHomeClick = {navController.navigate(AppScreen.UserHome.name)},
                        onProfileClick = {navController.navigate(AppScreen.UserProfile.name)}
                    )
                }

                composable(route = AppScreen.UserConfirmBook.name) {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    var dates by remember { mutableStateOf(emptyList<String>()) }
                    var times by remember { mutableStateOf(emptyList<String>()) }
                    var selectedDate by remember { mutableStateOf(LocalDate.now().format(formatter)) }
                    var selectedTime by remember { mutableStateOf("") }
                    var remark by remember { mutableStateOf("") }

                    LaunchedEffect(selectedDoctor) {
                        val dayOffList = selectedDoctor?.dayOff?.split(",")?.map { it.trim() } ?: emptyList()
                        dates = getAvailableDate(dayOffList)
                    }

                    LaunchedEffect(selectedDate) {
                        selectedTime = ""
                        val bookedTimes = appointmentsViewModel.getBookedTimes(selectedDoctor?.id ?: "", selectedDate)
                        times = getAvailableTime(bookedTimes, selectedDate)
                    }

                    UserConfirmBookScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        doctor = selectedDoctor,
                        dates = dates,
                        times = times,
                        selectedDate = selectedDate,
                        onChangeSelectedDate = {selectedDate = it},
                        selectedTime = selectedTime,
                        onChangeSelectedTime = {selectedTime = it},
                        remark = remark,
                        onChangeRemark = {remark = it},
                        onConfirmClick = {
                            val appointment = Appointments(
                                doctorID = selectedDoctor?.id ?: "",
                                userIC = currentUser?.ic ?: "",
                                date = selectedDate,
                                time = selectedTime,
                                complaint = remark,
                                status = "Pending"
                            )

                            appointmentsViewModel.insert(appointment)
                            navController.navigate(AppScreen.UserSuccessBook.name)
                        }
                    )
                }


                composable(route = AppScreen.UserSuccessBook.name) {
                    UserSuccessBookScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        onBackClick = { navController.navigate(AppScreen.UserHome.name) }
                    )
                }

                composable(route = AppScreen.UserViewAppointment.name) {
                    LaunchedEffect(Unit) {
                        appointmentsViewModel.getAppointmentByUserIC(currentUser?.ic ?: "")
                    }

                    UserViewAppointmentScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        appointments = appointments
                    )
                }



                composable(route = AppScreen.UserMedicalReminder.name) {
                    LaunchedEffect(currentUser) {
                        usersViewModel.observeReminders(context)

                    }
                    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

                    val sortedReminders = remember(allMedicalReminders, selectedDate) {
                        allMedicalReminders
                            .filter { reminder ->
                                reminder.date.split(",").map { it.trim() }.contains(selectedDate.toString())
                            }
                            .sortedBy { it.time } // 按 time 排序
                    }

                    UserMedicReminderScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        selectedDate = selectedDate,
                        onChangeSelectedDate = {selectedDate = it},
                        reminders = sortedReminders,
                        currentScreen = currentScreen,
                        onHomeClick = {navController.navigate(AppScreen.UserHome.name)},
                        onProfileClick = {navController.navigate(AppScreen.UserProfile.name)},
                        onAppointmentClick = {navController.navigate(AppScreen.UserAppointment.name)},
                    )
                }

                composable(route = AppScreen.UserPersonalInfo.name) {
                    var name by remember { mutableStateOf(currentUser?.name ?: "") }
                    var ic by remember { mutableStateOf(currentUser?.ic ?: "") }
                    var age by remember { mutableStateOf(currentUser?.age?.toString() ?: "") }
                    var gender by remember { mutableStateOf(currentUser?.gender ?: "") }
                    var address by remember { mutableStateOf(currentUser?.address ?: "") }
                    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
                    var medicalHistory by remember { mutableStateOf(currentUser?.medicalHistory ?: "") }
                    var weight by remember { mutableStateOf(currentUser?.weight?.toString() ?: "") }
                    var height by remember { mutableStateOf(currentUser?.height?.toString() ?: "") }

                    UserPersonalInfoScreen(
                        modifier = modifier
                            .fillMaxHeight(),
                        name = name,
                        onChangeName = {name = it},
                        ic = ic,
                        age = age,
                        onChangeAge = {age = it},
                        gender = gender,
                        onChangeGender = {gender = it},
                        phone = phone,
                        onChangePhone = {phone = it},
                        address = address,
                        onChangeAddress = {address = it},
                        weight = weight,
                        onChangeWeight = {weight = it},
                        height = height,
                        onChangeHeight = {height = it},
                        medicalHistory = medicalHistory,
                        onChangeMedicalHistory = {medicalHistory = it},
                        onSaveClick = {
                            val updateState = currentUser?.copy(
                                name = name,
                                age = age.toIntOrNull() ?: 0,
                                gender = gender,
                                phone = phone,
                                address = address,
                                medicalHistory = medicalHistory,
                                weight = weight.toDoubleOrNull() ?: 0.0,
                                height = height.toDoubleOrNull() ?: 0.0
                            )
                            updateState?.let {
                                usersViewModel.update(updateState)
                            }
                            navController.navigate(AppScreen.UserProfile.name)
                        },
                        onBackClick = {navController.navigate(AppScreen.UserProfile.name)}
                    )
                }
            }



        }


    }

}

@RequiresApi(Build.VERSION_CODES.O)
private fun calculateMedicationDates(med: MedicationUiState): List<String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return when (med.repeatType) {
        "Daily" -> {
            val start = LocalDate.parse(med.startDate)
            val end = LocalDate.parse(med.endDate)
            generateSequence(start) { it.plusDays(1) }
                .takeWhile { !it.isAfter(end) }
                .map { it.format(formatter) }
                .toList()
        }
        "Weekly" -> {
            val start = LocalDate.parse(med.startDate)
            val weeks = med.interval.toIntOrNull() ?: 0
            val end = start.plusWeeks(weeks.toLong())
            val targetDays = med.weeklyDays.mapNotNull { day ->
                when (day) {
                    "Mon" -> DayOfWeek.MONDAY
                    "Tue" -> DayOfWeek.TUESDAY
                    "Wed" -> DayOfWeek.WEDNESDAY
                    "Thu" -> DayOfWeek.THURSDAY
                    "Fri" -> DayOfWeek.FRIDAY
                    "Sat" -> DayOfWeek.SATURDAY
                    "Sun" -> DayOfWeek.SUNDAY
                    else -> null
                }
            }

            generateSequence(start) { it.plusDays(1) }
                .takeWhile { !it.isAfter(end) }
                .filter { it.dayOfWeek in targetDays }
                .map { it.format(formatter) }
                .toList()
        }
        "Monthly" -> {
            val start = LocalDate.parse(med.startDate)
            val months = med.interval.toIntOrNull() ?: 0
            (0 until months).map { i ->
                start.plusMonths(i.toLong()).format(formatter)
            }
        }
        else -> emptyList()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getAvailableDate(dayOff: List<String>): List<String> {
    val totalDays = 10
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayOffSet = dayOff.mapNotNull {
        try {
            DayOfWeek.valueOf(it.uppercase())
        } catch (e: Exception) { null }
    }.toSet()

    val dates = mutableListOf<String>()
    var currentDate = LocalDate.now()

    while (dates.size < totalDays) {
        if (!dayOffSet.contains(currentDate.dayOfWeek)) {
            dates.add(currentDate.format(formatter))
        }
        currentDate = currentDate.plusDays(1)
    }

    return dates

}

@RequiresApi(Build.VERSION_CODES.O)
private fun getAvailableTime(
    bookedTimes: List<String>,
    selectedDate: String
): List<String> {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val start = LocalTime.of(8, 0)   // 08:00 AM
    val end = LocalTime.of(18, 0)    // 06:00 PM

    val today = LocalDate.now()
    val selected = LocalDate.parse(selectedDate)
    val now = LocalTime.now()

    val times = mutableListOf<String>()
    var current = start

    while (!current.isAfter(end)) {
        val timeStr = current.format(formatter)

        val isTooEarlyToday = selected.isEqual(today) && current.isBefore(now.plusHours(1))

        if (!bookedTimes.contains(timeStr) && !isTooEarlyToday) {
            times.add(timeStr)
        }
        current = current.plusMinutes(30)
    }

    return times
}
