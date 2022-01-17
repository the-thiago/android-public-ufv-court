package com.ufv.court.ui_myschedule.myschedule

data class MyScheduleViewState(
    val error: Throwable? = null
) {
    companion object {
        val Empty = MyScheduleViewState()
    }
}
