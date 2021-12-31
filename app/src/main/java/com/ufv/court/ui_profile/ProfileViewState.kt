package com.ufv.court.ui_profile

data class ProfileViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val showConfirmLogoutDialog: Boolean = false,
    val email: String = "thiago.souza2@ufv.br", // todo get from back
    val name: String = "Thiago Souza",  // todo get from back
    val imageUrl: String = "https://picsum.photos/id/46/200/300"
) {
    companion object {
        val Empty = ProfileViewState()
    }
}

sealed class ProfileError : Exception() {
    object LogoutError : ProfileError()
}
