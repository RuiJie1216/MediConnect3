package com.example.mylastapp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPwdViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank()) {
            onResult(false, "Email cannot be empty")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onResult(true, "Password reset email sent successfully")
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to send password reset email: ${exception.message}")
            }
    }

}