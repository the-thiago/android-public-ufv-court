package com.ufv.court.ui_profile.changepasword

data class ChangePasswordViewState(
    val error: Throwable? = null
) {
    companion object {
        val Empty = ChangePasswordViewState()
    }
}