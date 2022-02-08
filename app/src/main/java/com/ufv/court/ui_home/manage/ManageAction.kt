package com.ufv.court.ui_home.manage

sealed class ManageAction {
    object CleanErrors : ManageAction()
    data class ChangeSelectedDate(val date: String) : ManageAction()
}
