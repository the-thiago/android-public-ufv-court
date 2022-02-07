package com.ufv.court.ui_login.register

import android.net.Uri

sealed class RegisterAction {
    object CleanErrors : RegisterAction()
    data class ChangeEmail(val email: String) : RegisterAction()
    data class ChangePassword(val password: String) : RegisterAction()
    data class ChangeConfirmPassword(val confirmPassword: String) : RegisterAction()
    data class ChangePhone(val phone: String) : RegisterAction()
    data class ChangeName(val name: String) : RegisterAction()
    data class ChangeImageUri(val uri: Uri) : RegisterAction()
    data class ShowEmailSentDialog(val show: Boolean) : RegisterAction()
    object RegisterCredentials : RegisterAction()
}