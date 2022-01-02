package com.ufv.court.ui_login.registeruser

sealed class RegisterUserAction {
    object CleanErrors : RegisterUserAction()
    data class ChangeName(val name: String) : RegisterUserAction()
    data class ContinueClick(val onSuccess: (String) -> Unit) : RegisterUserAction()
}
