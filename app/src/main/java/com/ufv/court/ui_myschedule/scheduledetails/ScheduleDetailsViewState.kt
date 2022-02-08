package com.ufv.court.ui_myschedule.scheduledetails

import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.user_service.domain.model.UserModel

data class ScheduleDetailsViewState(
    val error: Throwable? = null,
    val scheduleId: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val placeholder: Boolean = true,
    val schedule: ScheduleModel? = null,
    val user: UserModel? = null,
    val showCancellationDialog: Boolean = false,
    val showCancelledDialog: Boolean = false,
    val showParticipatingDialog: Boolean = false,
    val showCancelParticipationDialog: Boolean = false,
    val isTheOwner: Boolean = false,
    val isParticipating: Boolean = false,
    val eventComments: ScheduleComments = ScheduleComments(),
    val comment: String = "",
    val isSendingComment: Boolean = false,
    val showCommentSent: Boolean = false,
    val showDeleteCommentDialog: Boolean = false,
    val showDeletedCommentDialog: Boolean = false
) {
    companion object {
        val Empty = ScheduleDetailsViewState()
    }
}
