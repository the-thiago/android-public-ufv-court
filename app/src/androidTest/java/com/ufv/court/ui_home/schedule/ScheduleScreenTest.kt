package com.ufv.court.ui_home.schedule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class ScheduleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                ScheduleScreen(ScheduleViewState(placeholder = true), {}, {}, {})
            }
            onNodeWithTag("CircularLoading", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotLoadingStateThenCircularLoadingDoesNotExist() {
        composeTestRule.run {
            setContent {
                ScheduleScreen(ScheduleViewState(placeholder = false), {}, {}, {})
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenScheduleIsCreatedThenSelectTimeIsClickable() {
        composeTestRule.run {
            setContent {
                ScheduleScreen(ScheduleViewState(placeholder = false), {}, {}, {})
            }
            onNodeWithTag("SelectTime", true).assertIsDisplayed().assertHasClickAction()
        }
    }

    @Test
    fun whenScheduleIsCreatedThenTypeOfEventIsDisplayed() {
        composeTestRule.run {
            setContent {
                ScheduleScreen(ScheduleViewState(placeholder = false), {}, {}, {})
            }
            onNodeWithTag("TypeOfEvent", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenPerformTextInputThenTheTitleTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(ScheduleViewState(placeholder = false)) }
                ScheduleScreen(
                    state = state,
                    navigateUp = {},
                    openHome = {},
                    action = {
                        if (it is ScheduleAction.ChangeTitle) {
                            state = state.copy(title = it.title)
                        }
                    }
                )
            }
            onNodeWithTag("TitleTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("TitleTextField", true).onChildAt(1).assertTextEquals("a")
        }
    }

    @Test
    fun whenPerformTextInputThenTheDescriptionTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(ScheduleViewState(placeholder = false)) }
                ScheduleScreen(
                    state = state,
                    navigateUp = {},
                    openHome = {},
                    action = {
                        if (it is ScheduleAction.ChangeDescription) {
                            state = state.copy(description = it.description)
                        }
                    }
                )
            }
            onNodeWithTag("DescriptionTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("DescriptionTextField", true).onChildAt(1).assertTextEquals("a")
        }
    }

    @Test
    fun whenScheduleIsCreatedThenSpaceFreeTextIsDisplayed() {
        composeTestRule.run {
            setContent {
                ScheduleScreen(ScheduleViewState(placeholder = false), {}, {}, {})
            }
            onNodeWithTag("FreeSpaceText", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenFreeSpaceClickThenTheCheckBoxValueChanges() {
        var hasFreeSpace = false
        composeTestRule.run {
            setContent {
                var state by remember { mutableStateOf(ScheduleViewState(placeholder = false)) }
                ScheduleScreen(
                    state = state,
                    navigateUp = {},
                    openHome = {},
                    action = {
                        if (it is ScheduleAction.ChangeHasFreeSpace) {
                            hasFreeSpace = it.has
                            state = state.copy(hasFreeSpace = it.has)
                        }
                    }
                )
            }
            onNodeWithTag("FreeSpaceCheckBox", true).performClick()
            assert(hasFreeSpace).equals(true)
        }
    }
}
