package com.ufv.court.ui_login.registeruser

import android.net.Uri

data class RegisterUserViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val name: String = "",
    val imageUri: Uri? = null,
) {
    companion object {
        val Empty = RegisterUserViewState()
    }
}

sealed class RegisterUserError : Exception() {
    object EmptyName : RegisterUserError()
}
