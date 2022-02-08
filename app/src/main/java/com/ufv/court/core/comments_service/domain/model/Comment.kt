package com.ufv.court.core.comments_service.domain.model

data class Comment(
    val userId: String = "",
    val userName: String = "",
    val userPhoto: String = "",
    val time: Long = 0L,
    val text: String = ""
)
