package com.ufv.court.core.user_service.domain.model

import android.net.Uri

data class User(
    val isLogged: Boolean,
    val name: String,
    val email: String,
    val photoUrl: Uri
)