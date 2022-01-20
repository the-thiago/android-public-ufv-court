package com.ufv.court.ui_profile.editprofile

import android.net.Uri

data class EditProfileViewState(
    val error: Throwable? = null,
    val placeholder: Boolean = true,
    val isLoading: Boolean = false,
    val currentImage: String = "",
    val name: String = "",
    val newImageUri: Uri? = null,
    val profileEditedDialog: Boolean = false,
) {
    companion object {
        val Empty = EditProfileViewState()
    }
}

sealed class EditProfileError : Exception() {
    object EmptyField : EditProfileError()
}
