package com.ufv.court.core.user_service.data_remote.request

import android.net.Uri

data class RegisterUser(
    val name: String,
    val email: String,
    val password: String,
    val photo: Uri
)
