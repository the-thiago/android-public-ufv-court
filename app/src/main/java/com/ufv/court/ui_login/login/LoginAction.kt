package com.ufv.court.ui_login.login

sealed class LoginAction {
    object CleanErrors : LoginAction()
    object Login : LoginAction()
    data class ChangeEmailText(val email: String) : LoginAction()
    data class ChangePasswordText(val password: String) : LoginAction()
    object ChangePasswordVisibility : LoginAction()
    object RegisterAccountClick : LoginAction()
    object ForgotPasswordClick : LoginAction()
}