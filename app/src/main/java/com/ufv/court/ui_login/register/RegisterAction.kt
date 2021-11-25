package com.ufv.court.ui_login.register

sealed class RegisterAction {
    data class ChangeEmailText(val email: String) : RegisterAction()
    data class ChangePasswordText(val password: String) : RegisterAction()
}