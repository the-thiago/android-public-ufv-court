package com.ufv.court.ui_login.login

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

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateThenButtonIsLoading() {
        composeTestRule.run {
            setContent {
                LoginScreen(LoginViewState(isLoading = true), {}, {}, {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertDoesNotExist()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotLoadingStateThenButtonIsNotLoading() {
        composeTestRule.run {
            setContent {
                LoginScreen(LoginViewState(isLoading = false), {}, {}, {}, {})
            }
            onNodeWithTag("TextLoadingButton", true).assertIsDisplayed()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenEmptyFieldErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.empty_field_error)
                LoginScreen(LoginViewState(LoginError.EmptyField), {}, {}, {}, {})
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
                LoginScreen(LoginViewState(LoginError.InvalidCredentials), {}, {}, {}, {})
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
                LoginScreen(LoginViewState(LoginError.NoUserFound), {}, {}, {}, {})
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenEmailNotVerifiedErrorThenDisplayOneButtonErrorDialog() {
        var message = ""
        val email = "test@gmail.com"
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.email_not_verified, email)
                LoginScreen(
                    LoginViewState(error = LoginError.EmailNotVerified, email = email),
                    {},
                    {},
                    {},
                    {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenPerformTextInputThenTheEmailTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(LoginViewState()) }
                LoginScreen(
                    state = state,
                    openHome = {},
                    openForgotPassword = {},
                    openRegister = {},
                    action = {
                        if (it is LoginAction.ChangeEmailText) {
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
                var state by remember { mutableStateOf(LoginViewState(isPasswordVisible = true)) }
                LoginScreen(
                    state = state,
                    openHome = {},
                    openForgotPassword = {},
                    openRegister = {},
                    action = {
                        if (it is LoginAction.ChangePasswordText) {
                            state = state.copy(password = it.password)
                        }
                    }
                )
            }
            onNodeWithTag("PasswordTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("PasswordTextField", true).onChildAt(1).assertTextEquals("a")
        }
    }
}
