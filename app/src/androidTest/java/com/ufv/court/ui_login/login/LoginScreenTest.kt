package com.ufv.court.ui_login.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.ui_login.forgotpassword.ForgotPasswordScreen
import com.ufv.court.ui_login.forgotpassword.ForgotPasswordViewState
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

//    @Test
//    fun whenLoadingStateThenButtonIsLoading() {
//        composeTestRule.run {
//            setContent {
//                LoginScreen(LoginViewState(isLoading = true), {}, {})
//            }
//            onNodeWithTag("TextLoadingButton", true).assertDoesNotExist()
//            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun whenNotLoadingStateThenButtonIsNotLoading() {
//        composeTestRule.run {
//            setContent {
//                ForgotPasswordScreen(ForgotPasswordViewState(isLoading = false), {}, {})
//            }
//            onNodeWithTag("TextLoadingButton", true).assertIsDisplayed()
//            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertDoesNotExist()
//        }
//    }
}