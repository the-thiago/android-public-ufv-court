package com.ufv.court.ui_profile.changepasword

sealed class ChangePasswordAction {
    object CleanErrors : ChangePasswordAction()
    data class ChangeShowPasswordChangedDialog(val show: Boolean) : ChangePasswordAction()
    data class ChangeCurrentPassword(val currentPassword: String) : ChangePasswordAction()
    data class ChangeNewPassword(val newPassword: String) : ChangePasswordAction()
    data class ChangeConfirmPassword(val confirmPassword: String) : ChangePasswordAction()
    object ChangePasswordClick : ChangePasswordAction()
}
