package com.ufv.court.ui_profile.changepasword

data class ChangePasswordViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val showPasswordChangedDialog: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = ""
) {
    companion object {
        val Empty = ChangePasswordViewState()
    }
}

sealed class ChangePasswordError : Exception() {
    object EmptyField : ChangePasswordError()
    object DifferentPasswords : ChangePasswordError()
    object InvalidCredentials : ChangePasswordError()
    object NoUserFound : ChangePasswordError()
    object AuthWeakPassword : ChangePasswordError()
}
