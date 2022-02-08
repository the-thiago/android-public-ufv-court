package com.ufv.court.core.comments_service.data_remote.data_sources

import com.ufv.court.core.comments_service.data.data_sourcers.CommentsDataSource
import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import javax.inject.Inject

internal class CommentsRemoteDataSourceImpl @Inject constructor() : CommentsDataSource {

    private val commentsPath = "comments"

    override suspend fun getComments(eventId: String): ScheduleComments {
        TODO("Not yet implemented")
    }
}
