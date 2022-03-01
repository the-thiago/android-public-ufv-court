package com.ufv.court.ui_profile.editprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class EditProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenPlaceHolderIsTrueThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                EditProfileScreen(
                    state = EditProfileViewState(placeholder = true),
                    navigateUp = {},
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
                EditProfileScreen(
                    state = EditProfileViewState(placeholder = false),
                    navigateUp = {},
                    action = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenLoadingStateThenButtonIsLoading() {
        composeTestRule.run {
            setContent {
                EditProfileScreen(
                    state = EditProfileViewState(placeholder = false, isLoading = true),
                    navigateUp = {},
                    action = {}
                )
            }
            onNodeWithTag("TextLoadingButton", true).assertDoesNotExist()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotLoadingStateThenButtonIsNotLoading() {
        composeTestRule.run {
            setContent {
                EditProfileScreen(
                    state = EditProfileViewState(placeholder = false, isLoading = false),
                    navigateUp = {},
                    action = {}
                )
            }
            onNodeWithTag("TextLoadingButton", true).assertIsDisplayed()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenPerformTextInputThenTheNameTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(EditProfileViewState(placeholder = false)) }
                EditProfileScreen(
                    state = state,
                    navigateUp = {},
                    action = {
                        if (it is EditProfileAction.ChangeName) {
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
    fun whenPerformTextInputThenThePhoneTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(EditProfileViewState(placeholder = false)) }
                EditProfileScreen(
                    state = state,
                    navigateUp = {},
                    action = {
                        if (it is EditProfileAction.ChangePhone) {
                            state = state.copy(phone = it.phone)
                        }
                    }
                )
            }
            onNodeWithTag("PhoneTextField", true).onChildAt(1).performTextInput("1")
            onNodeWithTag("PhoneTextField", true).onChildAt(1).assertTextEquals("(1")
        }
    }
}
