package com.ufv.court.ui_login.registercredentials

data class RegisterCredentialsViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showEmailSentDialog: Boolean = false
) {
    companion object {
        val Empty = RegisterCredentialsViewState()
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