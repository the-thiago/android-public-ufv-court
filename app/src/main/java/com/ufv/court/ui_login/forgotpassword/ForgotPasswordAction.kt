package com.ufv.court.ui_login.forgotpassword

sealed class ForgotPasswordAction {
    object CleanErrors : ForgotPasswordAction()
    data class ChangeEmail(val email: String) : ForgotPasswordAction()
    data class ChangeShowInfoDialog(val show: Boolean) : ForgotPasswordAction()
    data class ChangeShowEmailSentDialog(val show: Boolean) : ForgotPasswordAction()
    object SendEmail : ForgotPasswordAction()
}
