package com.ufv.court.ui_login.login

sealed class LoginAction {
    object CleanErrors : LoginAction()
    data class Login(val onSuccess: () -> Unit) : LoginAction()
    data class ChangeEmailText(val email: String) : LoginAction()
    data class ChangePasswordText(val password: String) : LoginAction()
    object ChangePasswordVisibility : LoginAction()
    object ForgotPasswordClick : LoginAction()
}