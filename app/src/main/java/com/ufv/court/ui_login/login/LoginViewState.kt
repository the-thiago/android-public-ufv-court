package com.ufv.court.ui_login.login

data class LoginViewState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
) {
    companion object {
        val Empty = LoginViewState()
    }
}