package com.ufv.court.ui_login.register

import android.net.Uri

data class RegisterViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val name: String = "",
    val imageUri: Uri? = null,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showEmailSentDialog: Boolean = false
) {
    companion object {
        val Empty = RegisterViewState()
    }
}

sealed class RegisterCredentialsError : Exception() {
    object DifferentPassword : RegisterCredentialsError()
    object EmptyField : RegisterCredentialsError()
    object EmailDomainNotAllowed : RegisterCredentialsError()
    object AuthWeakPassword : RegisterCredentialsError()
    object AuthUserCollision : RegisterCredentialsError()
    object AuthInvalidCredentials : RegisterCredentialsError()
    object SendEmailVerification : RegisterCredentialsError()
}