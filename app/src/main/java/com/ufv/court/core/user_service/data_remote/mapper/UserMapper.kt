package com.ufv.court.core.user_service.data_remote.mapper

import com.google.firebase.auth.FirebaseUser
import com.ufv.court.core.user_service.domain.model.UserModel

fun FirebaseUser?.toModel(image: String): UserModel = UserModel(
    id = this?.uid ?: "",
    name = this?.displayName ?: "",
    email = this?.email ?: "",
    image = image
)
