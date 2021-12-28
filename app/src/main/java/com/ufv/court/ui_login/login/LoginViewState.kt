package com.ufv.court.ui_login.login

data class LoginViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
) {
    companion object {
        val Empty = LoginViewState()
    }
}

sealed class LoginError : Exception() {
    object EmptyField : LoginError()
    object InvalidCredentials : LoginError()
    object NoUserFound : LoginError()
    object EmailNotVerified : LoginError()
}