package com.ufv.court.ui_login.register

sealed class RegisterAction {
    object NavigateUp : RegisterAction()
    object CleanErrors : RegisterAction()
    data class ChangeEmail(val email: String) : RegisterAction()
    data class ChangePassword(val password: String) : RegisterAction()
    data class ChangeConfirmPassword(val confirmPassword: String) : RegisterAction()
    object Register : RegisterAction()
}