package com.ufv.court.ui_login.register

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

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateThenButtonIsLoading() {
        composeTestRule.run {
            setContent {
                RegisterScreen(RegisterViewState(isLoading = true), {}, {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertDoesNotExist()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertExists()
        }
    }

    @Test
    fun whenNotLoadingStateThenButtonIsNotLoading() {
        composeTestRule.run {
            setContent {
                RegisterScreen(RegisterViewState(isLoading = false), {}, {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertExists()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenDifferentPasswordErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.different_passwords)
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.DifferentPassword), {}, {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenEmptyFieldErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.empty_field_error)
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.EmptyField), {}, {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenEmailDomainNotAllowedErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.email_domain_not_allowed)
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.EmailDomainNotAllowed), {}, {}, {}
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
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.AuthWeakPassword), {}, {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenAuthUserCollisionErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.user_already_exists)
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.AuthUserCollision), {}, {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenAuthInvalidCredentialsErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.invalid_credentials)
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.AuthInvalidCredentials), {}, {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenSendEmailVerificationErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.verification_email_error)
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.SendEmailVerification), {}, {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenInvalidPhoneErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.invalid_phone)
                RegisterScreen(
                    RegisterViewState(RegisterCredentialsError.InvalidPhone), {}, {}, {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenPerformTextInputThenTheNameTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(RegisterViewState()) }
                RegisterScreen(
                    state = state,
                    navigateUp = {},
                    openLogin = {},
                    action = {
                        if (it is RegisterAction.ChangeName) {
                            state = state.copy(name = it.name)
                        }
                    }
                )
            }
            onNodeWithTag("NameTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("NameTextField", true).onChildAt(1).assertTextEquals("a")
        }
    }

    @Test
    fun whenPerformTextInputThenTheEmailTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(RegisterViewState()) }
                RegisterScreen(
                    state = state,
                    navigateUp = {},
                    openLogin = {},
                    action = {
                        if (it is RegisterAction.ChangeEmail) {
                            state = state.copy(email = it.email)
                        }
                    }
                )
            }
            onNodeWithTag("EmailTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("EmailTextField", true).onChildAt(1).assertTextEquals("a")
        }
    }

    @Test
    fun whenPerformTextInputThenThePasswordTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(RegisterViewState()) }
                RegisterScreen(
                    state = state,
                    navigateUp = {},
                    openLogin = {},
                    action = {
                        if (it is RegisterAction.ChangePassword) {
                            state = state.copy(password = it.password)
                        }
                    }
                )
            }
            onNodeWithTag("PasswordTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("PasswordTextField", true).onChildAt(1).assertTextEquals("•")
        }
    }

    @Test
    fun whenEmailSentThenDialogIsDisplayed() {
        var message = ""
        val email = "teste@gmail.com"
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.email_sent, email)
                RegisterScreen(
                    state = RegisterViewState(
                        showEmailSentDialog = true,
                        email = email
                    ),
                    navigateUp = {},
                    openLogin = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }
}
