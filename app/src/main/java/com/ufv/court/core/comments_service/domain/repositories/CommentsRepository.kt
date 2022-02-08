package com.ufv.court.core.comments_service.domain.repositories

import com.ufv.court.core.comments_service.domain.model.ScheduleComments

interface CommentsRepository {

    suspend fun getComments(eventId: String): ScheduleComments

    suspend fun sendComment(eventComments: ScheduleComments)
}
