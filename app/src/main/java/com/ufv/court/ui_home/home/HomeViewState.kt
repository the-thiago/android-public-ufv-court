package com.ufv.court.ui_home.home

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

data class HomeViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val schedules: List<ScheduleModel> = listOf()
) {
    companion object {
        val Empty = HomeViewState()
    }
}
