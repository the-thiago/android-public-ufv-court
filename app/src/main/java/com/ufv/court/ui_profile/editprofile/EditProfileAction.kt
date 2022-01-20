package com.ufv.court.ui_profile.editprofile

import android.net.Uri

sealed class EditProfileAction {
    object CleanErrors : EditProfileAction()
    data class ChangeName(val name: String) : EditProfileAction()
    data class ChangeImageUri(val uri: Uri) : EditProfileAction()
    object SaveProfile : EditProfileAction()
}
