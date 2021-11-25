package com.ufv.court.ui_login.register

data class RegisterViewState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
) {
    companion object {
        val Empty = RegisterViewState()
    }
}