package com.ufv.court.ui_home.manage

data class ManageViewState(
    val error: Throwable? = null,
) {
    companion object {
        val Empty = ManageViewState()
    }
}
