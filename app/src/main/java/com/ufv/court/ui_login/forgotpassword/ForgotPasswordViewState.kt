package com.ufv.court.ui_login.forgotpassword

data class ForgotPasswordViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val email: String = "",
    val showInfoDialog: Boolean = false,
    val showEmailSentDialog: Boolean = false
) {
    companion object {
        val Empty = ForgotPasswordViewState()
    }
}

sealed class ForgotPasswordError : Exception() {
    object InvalidEmail : ForgotPasswordError()
    object NoUserFound : ForgotPasswordError()
}