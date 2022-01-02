package com.ufv.court.ui_login.registeruser

data class RegisterUserViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val name: String = "",
    val photo: String = "",
) {
    companion object {
        val Empty = RegisterUserViewState()
    }
}

sealed class RegisterUserError : Exception() {
    object EmptyName : RegisterUserError()
}
