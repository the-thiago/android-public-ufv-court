package com.ufv.court.core.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class LoadingButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateChangesThenUpdatesButtonUI() {
        composeTestRule.run {
            setContent {
                var isLoading by remember {
                    mutableStateOf(false)
                }
                LoadingButton(isLoading = isLoading, buttonText = "Click me") {
                    isLoading = !isLoading
                }
            }
            onNodeWithTag("TextLoadingButton", true).assertIsDisplayed()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertDoesNotExist()
            onNodeWithTag("LoadingButton").performClick()
            onNodeWithTag("TextLoadingButton", true).assertDoesNotExist()
            onNodeWithTag("ProgressIndicatorLoadingButton", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenIsNotLoadingStateThenShowsTextButtonAndIsEnabled() {
        composeTestRule.run {
            val buttonText = "My text"
            setContent {
                LoadingButton(isLoading = false, buttonText = buttonText) { }
            }
            onNodeWithTag("TextLoadingButton", true).assertTextEquals(
                buttonText.uppercase()
            )
            onNodeWithTag("LoadingButton").assertIsEnabled()
        }
    }

    @Test
    fun whenLoadingStateThenButtonIsNotEnabled() {
        composeTestRule.run {
            setContent {
                LoadingButton(isLoading = true, buttonText = "Click me") { }
            }
            onNodeWithTag("LoadingButton").assertIsNotEnabled()
        }
    }
}
