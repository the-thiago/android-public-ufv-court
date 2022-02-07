package com.ufv.court.ui_profile.profile

sealed class ProfileAction {
    object CleanErrors : ProfileAction()
    data class ChangeShowConfirmLogoutDialog(val show: Boolean) : ProfileAction()
    data class ConfirmLogout(val onSuccess: () -> Unit) : ProfileAction()
    object Refresh : ProfileAction()
}
