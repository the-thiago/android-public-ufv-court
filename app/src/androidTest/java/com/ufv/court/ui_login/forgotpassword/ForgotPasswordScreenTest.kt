package com.ufv.court.ui_login.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.ufv.court.R
import org.junit.Rule
import org.junit.Test

class ForgotPasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateThenButtonIsLoading() {
        composeTestRule.run {
            setContent {
                ForgotPasswordScreen(ForgotPasswordViewState(isLoading = true), {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertDoesNotExist()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotLoadingStateThenButtonIsNotLoading() {
        composeTestRule.run {
            setContent {
                ForgotPasswordScreen(ForgotPasswordViewState(isLoading = false), {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertIsDisplayed()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenInvalidEmailErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.type_a_valid_email)
                ForgotPasswordScreen(
                    ForgotPasswordViewState(ForgotPasswordError.InvalidEmail), {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenNoUserFoundErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.email_not_registered)
                ForgotPasswordScreen(
                    ForgotPasswordViewState(ForgotPasswordError.NoUserFound), {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenShowInfoDialogIsTrueThenDialogIsDisplayed() {
        var title = ""
        var message = ""
        composeTestRule.run {
            setContent {
                title = stringResource(id = R.string.reset_password)
                message = stringResource(id = R.string.email_will_be_sent)
                ForgotPasswordScreen(ForgotPasswordViewState(showInfoDialog = true), {}, {})
            }
            onNodeWithTag("TitleOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(title)
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenEmailSentThenDialogIsDisplayed() {
        var message = ""
        val email = "teste@gmail.com"
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.email_sent_to, email)
                ForgotPasswordScreen(
                    state = ForgotPasswordViewState(
                        showEmailSentDialog = true,
                        email = email
                    ),
                    navigateUp = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenPerformTextInputThenTheTeamNameTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(ForgotPasswordViewState()) }
                ForgotPasswordScreen(
                    state = state,
                    navigateUp = {},
                    action = {
                        if (it is ForgotPasswordAction.ChangeEmail) {
                            state = state.copy(email = it.email)
                        }
                    }
                )
            }
            onNodeWithTag("EmailTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("EmailTextField", true).onChildAt(1).assertTextEquals("a")
        }
    }
}
