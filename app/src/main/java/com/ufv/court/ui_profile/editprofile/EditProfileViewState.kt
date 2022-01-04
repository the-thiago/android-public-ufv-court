package com.ufv.court.ui_profile.editprofile

data class EditProfileViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val name: String = "",
) {
    companion object {
        val Empty = EditProfileViewState()
    }
}
