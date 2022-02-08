package com.ufv.court.core.user_service.domain.model

data class UserModel(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val image: String = "",
    val admin: Boolean = false
)
