package com.ufv.court.ui_login.register

data class RegisterViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
) {
    companion object {
        val Empty = RegisterViewState()
    }
}

sealed class RegisterError : Exception() {
    object DifferentPassword : RegisterError()
    object EmptyField : RegisterError()
    object EmailDomainNotAllowed : RegisterError()
}