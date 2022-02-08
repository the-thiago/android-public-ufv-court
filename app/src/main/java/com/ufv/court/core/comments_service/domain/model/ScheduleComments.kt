package com.ufv.court.core.comments_service.domain.model

data class ScheduleComments(
    val id: String = "",
    val eventId: String = "",
    val comments: List<Comment> = listOf()
)
