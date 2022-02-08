package com.ufv.court.core.comments_service.data.data_sourcers

import com.ufv.court.core.comments_service.domain.model.ScheduleComments

interface CommentsDataSource {

    suspend fun getComments(eventId: String): ScheduleComments

    suspend fun sendComment(eventComments: ScheduleComments)

    suspend fun createEventComments(eventComments: ScheduleComments)
}
