package com.ufv.court.core.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class CustomToolbarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenToolbarTextIsEmptyThenToolbarTextDoesNotExist() {
        composeTestRule.run {
            setContent {
                CustomToolbar(toolbarText = "") { }
            }
            onNodeWithTag("CustomToolbarText").assertDoesNotExist()
        }
    }

    @Test
    fun whenToolbarTextIsNotEmptyThenToolbarTextIsDisplayed() {
        val text = "My Toolbar"
        composeTestRule.run {
            setContent {
                CustomToolbar(toolbarText = text) { }
            }
            onNodeWithTag("CustomToolbarText").assertIsDisplayed().assertTextEquals(text)
        }
    }

    @Test
    fun whenRightIconIsNullThenRightButtonDoesNotExit() {
        composeTestRule.run {
            setContent {
                CustomToolbar(rightIcon = null) { }
            }
            onNodeWithTag("CustomToolbarRightIcon").assertDoesNotExist()
        }
    }

    @Test
    fun whenRightIconIsNotNullThenRightButtonExitAndHasClickAction() {
        composeTestRule.run {
            setContent {
                CustomToolbar(
                    rightIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    onLeftButtonClick = { }
                )
            }
            onNodeWithTag("CustomToolbarRightIcon", true).assertIsDisplayed().assertHasClickAction()
        }
    }

    @Test
    fun whenToolbarIsCreatedThenLeftIconHasClickAction() {
        composeTestRule.run {
            setContent {
                CustomToolbar { }
            }
            onNodeWithTag("RoundedBackButton").assertIsDisplayed().assertHasClickAction()
        }
    }
}
