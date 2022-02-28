package com.ufv.court.ui_home.manage

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class ManageScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                ManageScreen(ManageViewState(placeholder = true), {}, {}, {})
            }
            onNodeWithTag("CircularLoading", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotLoadingStateThenCircularLoadingDoesNotExist() {
        composeTestRule.run {
            setContent {
                ManageScreen(ManageViewState(placeholder = false), {}, {}, {})
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }
}
