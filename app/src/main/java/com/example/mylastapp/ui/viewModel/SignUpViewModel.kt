package com.example.mylastapp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.mylastapp.ui.uiState.SignUpUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel: ViewModel() {

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.asStateFlow()

    fun reset() {
        _signUpUiState.update {
            SignUpUiState()
        }
    }

    fun setExistPatient(existPatient: Boolean) {
        _signUpUiState.update { current ->
            current.copy(
                existPatient = existPatient
            )
        }
    }

    fun setIc(ic: String) {
        _signUpUiState.update { current ->
            current.copy(
                ic = ic

            )
        }
    }


    fun setName(name: String) {
        _signUpUiState.update { current ->
            current.copy(
                name = name
            )
        }
    }

    fun setPwd(pwd: String) {
        _signUpUiState.update { current ->
            current.copy(
                pwd = pwd
            )
        }
    }

    fun setEmail(email: String) {
        _signUpUiState.update { current ->
            current.copy(
                email = email
            )
        }
    }

    fun setPhone(phone: String) {
        _signUpUiState.update { current ->
            current.copy(
                phone = phone
            )
        }

    }

    fun setRead(read: Boolean) {
        _signUpUiState.update { current ->
            current.copy(
                read = read
            )
        }
    }


}