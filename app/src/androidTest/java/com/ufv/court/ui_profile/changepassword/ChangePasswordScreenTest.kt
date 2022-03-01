package com.ufv.court.ui_profile.changepassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.ufv.court.R
import com.ufv.court.ui_profile.changepasword.ChangePasswordAction
import com.ufv.court.ui_profile.changepasword.ChangePasswordError
import com.ufv.court.ui_profile.changepasword.ChangePasswordScreen
import com.ufv.court.ui_profile.changepasword.ChangePasswordViewState
import org.junit.Rule
import org.junit.Test

class ChangePasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateThenButtonIsLoading() {
        composeTestRule.run {
            setContent {
                ChangePasswordScreen(ChangePasswordViewState(isLoading = true), {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertDoesNotExist()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotLoadingStateThenButtonIsNotLoading() {
        composeTestRule.run {
            setContent {
                ChangePasswordScreen(ChangePasswordViewState(isLoading = false), {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertIsDisplayed()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenPerformTextInputThenTheCurrentPasswordTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(ChangePasswordViewState()) }
                ChangePasswordScreen(
                    state = state,
                    navigateUp = {},
                    action = {
                        if (it is ChangePasswordAction.ChangeCurrentPassword) {
                            state = state.copy(currentPassword = it.currentPassword)
                        }
                    }
                )
            }
            onNodeWithTag("CurrentPasswordChangePassword", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("CurrentPasswordChangePassword", true).onChildAt(1).assertTextEquals("•")
        }
    }

    @Test
    fun whenPerformTextInputThenTheNewPasswordTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(ChangePasswordViewState()) }
                ChangePasswordScreen(
                    state = state,
                    navigateUp = {},
                    action = {
                        if (it is ChangePasswordAction.ChangeNewPassword) {
                            state = state.copy(newPassword = it.newPassword)
                        }
                    }
                )
            }
            onNodeWithTag("NewPasswordChangePassword", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("NewPasswordChangePassword", true).onChildAt(1).assertTextEquals("•")
        }
    }

    @Test
    fun whenPerformTextInputThenTheConfirmPasswordTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(ChangePasswordViewState()) }
                ChangePasswordScreen(
                    state = state,
                    navigateUp = {},
                    action = {
                        if (it is ChangePasswordAction.ChangeConfirmPassword) {
                            state = state.copy(confirmPassword = it.confirmPassword)
                        }
                    }
                )
            }
            onNodeWithTag("ConfirmPasswordChangePassword", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("ConfirmPasswordChangePassword", true).onChildAt(1).assertTextEquals("•")
        }
    }

    @Test
    fun whenShowPasswordChangedDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.you_can_use_the_new_password)
                ChangePasswordScreen(
                    state = ChangePasswordViewState(showPasswordChangedDialog = true),
                    navigateUp = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenEmptyFieldErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.empty_field_error)
                ChangePasswordScreen(
                    ChangePasswordViewState(ChangePasswordError.EmptyField), {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenDifferentPasswordsErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.different_passwords)
                ChangePasswordScreen(
                    ChangePasswordViewState(ChangePasswordError.DifferentPasswords), {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenInvalidCredentialsErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.invalid_credentials)
                ChangePasswordScreen(
                    ChangePasswordViewState(ChangePasswordError.InvalidCredentials), {}, {}
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
                message = stringResource(id = R.string.invalid_credentials)
                ChangePasswordScreen(
                    ChangePasswordViewState(ChangePasswordError.NoUserFound), {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenAuthWeakPasswordErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.invalid_number_of_characters)
                ChangePasswordScreen(
                    ChangePasswordViewState(ChangePasswordError.AuthWeakPassword), {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }
}
