package com.ufv.court.ui_profile.profile

data class ProfileViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val showConfirmLogoutDialog: Boolean = false,
    val email: String = "",
    val name: String = "",
    val imageUrl: String = "https://thispersondoesnotexist.com/image" // todo
) {
    companion object {
        val Empty = ProfileViewState()
    }
}

sealed class ProfileError : Exception() {
    object LogoutError : ProfileError()
}
