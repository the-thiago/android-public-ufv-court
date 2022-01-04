package com.ufv.court.core.user_service.domain.model

data class User(
    val isLogged: Boolean,
    val name: String,
    val email: String,
    val image: String
)