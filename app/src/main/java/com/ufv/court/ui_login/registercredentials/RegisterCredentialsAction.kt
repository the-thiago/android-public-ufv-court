package com.ufv.court.ui_login.registercredentials

sealed class RegisterCredentialsAction {
    object CleanErrors : RegisterCredentialsAction()
    data class ChangeEmail(val email: String) : RegisterCredentialsAction()
    data class ChangePassword(val password: String) : RegisterCredentialsAction()
    data class ChangeConfirmPassword(val confirmPassword: String) : RegisterCredentialsAction()
    data class ShowEmailSentDialog(val show: Boolean) : RegisterCredentialsAction()
    object RegisterCredentials : RegisterCredentialsAction()
}