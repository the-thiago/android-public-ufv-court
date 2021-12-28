package com.ufv.court.ui_home.home

data class HomeViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false
) {
    companion object {
        val Empty = HomeViewState()
    }
}