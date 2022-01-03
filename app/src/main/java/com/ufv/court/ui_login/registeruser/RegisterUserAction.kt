package com.ufv.court.ui_login.registeruser

import android.net.Uri

sealed class RegisterUserAction {
    object CleanErrors : RegisterUserAction()
    data class ChangeName(val name: String) : RegisterUserAction()
    data class ChangeImageUri(val uri: Uri) : RegisterUserAction()
    data class ContinueClick(val onSuccess: (String) -> Unit) : RegisterUserAction()
}
