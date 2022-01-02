package com.ufv.court.core.user_service.data_remote.mapper

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.ufv.court.core.user_service.domain.model.User

fun FirebaseUser?.toModel(): User = User(
    isLogged = this != null,
    name = this?.displayName ?: "",
    email = this?.email ?: "",
    photoUrl = this?.photoUrl ?: Uri.EMPTY
)