package com.ufv.court.core.user_service.data_remote.mapper

import com.google.firebase.auth.FirebaseUser
import com.ufv.court.core.user_service.domain.model.User

fun FirebaseUser?.toModel(): User = User(
    isLogged = this != null,
    name = this?.displayName ?: "",
    email = this?.email ?: ""
)