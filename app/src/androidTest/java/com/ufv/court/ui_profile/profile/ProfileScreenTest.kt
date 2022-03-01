package com.ufv.court.ui_profile.profile

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.R
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenPlaceHolderIsTrueThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                ProfileScreen(
                    state = ProfileViewState(placeholder = true),
                    openLogin = {},
                    openChangePassword = {},
                    openEditProfile = {},
                    openPhoto = {},
                    action = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenPlaceHolderIsFalseThenCircularLoadingDoesNotExist() {
        composeTestRule.run {
            setContent {
                ProfileScreen(
                    state = ProfileViewState(placeholder = false),
                    openLogin = {},
                    openChangePassword = {},
                    openEditProfile = {},
                    openPhoto = {},
                    action = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenProfileScreenIsCreatedThenUserInfoAreDisplayed() {
        val name = "Name Test"
        val email = "test@gmail.com"
        val phone = "(00) 99999-0000"
        composeTestRule.run {
            setContent {
                ProfileScreen(
                    state = ProfileViewState(
                        placeholder = false,
                        name = name,
                        email = email,
                        phone = phone
                    ),
                    openLogin = {},
                    openChangePassword = {},
                    openEditProfile = {},
                    openPhoto = {},
                    action = {}
                )
            }
            onNodeWithTag("NameProfile", true).assertTextEquals(name)
            onNodeWithTag("EmailProfile", true).assertTextEquals(email)
            onNodeWithTag("PhoneProfile", true).assertTextEquals(phone)
        }
    }

    @Test
    fun whenLogoutErrorThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.could_not_logout)
                ProfileScreen(
                    state = ProfileViewState(
                        placeholder = false,
                        error = ProfileError.LogoutError
                    ),
                    openLogin = {},
                    openChangePassword = {},
                    openEditProfile = {},
                    openPhoto = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenShowConfirmLogoutDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.really_want_to_logout)
                ProfileScreen(
                    state = ProfileViewState(
                        placeholder = false,
                        showConfirmLogoutDialog = true
                    ),
                    openLogin = {},
                    openChangePassword = {},
                    openEditProfile = {},
                    openPhoto = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageTwoButtonsDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }
}
