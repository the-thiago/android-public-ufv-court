package com.ufv.court.ui_myschedule.participants

import com.ufv.court.core.user_service.domain.model.UserModel

data class ParticipantsViewState(
    val error: Throwable? = null,
    val participants: List<UserModel>? = null
) {
    companion object {
        val Empty = ParticipantsViewState()
    }
}
