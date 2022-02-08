package com.ufv.court.core.comments_service.data.repositories

import com.ufv.court.core.comments_service.data.data_sourcers.CommentsDataSource
import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import com.ufv.court.core.comments_service.domain.repositories.CommentsRepository
import javax.inject.Inject

internal class CommentsRepositoryImpl @Inject constructor(
    private val dataSource: CommentsDataSource
) : CommentsRepository {

    override suspend fun getComments(eventId: String): ScheduleComments {
        return dataSource.getComments(eventId = eventId)
    }

    override suspend fun sendComment(eventComments: ScheduleComments) {
        return dataSource.sendComment(eventComments = eventComments)
    }

    override suspend fun createEventComments(eventComments: ScheduleComments) {
        return dataSource.createEventComments(eventComments = eventComments)
    }
}
