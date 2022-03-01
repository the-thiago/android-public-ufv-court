package com.ufv.court.ui_myschedule.editevent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.ufv.court.R
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import org.junit.Rule
import org.junit.Test

class EditEventScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenPlaceHolderIsTrueThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                EditEventScreen(
                    state = EditEventViewState(placeholder = true, schedule = ScheduleModel()),
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
                EditEventScreen(
                    state = EditEventViewState(placeholder = false, schedule = ScheduleModel()),
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
                EditEventScreen(
                    state = EditEventViewState(
                        placeholder = false, schedule = ScheduleModel(), isLoading = true
                    ),
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
                EditEventScreen(
                    state = EditEventViewState(
                        placeholder = false, schedule = ScheduleModel(), isLoading = false
                    ),
                    navigateUp = {},
                    action = {}
                )
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
                EditEventScreen(
                    state = EditEventViewState(error = EditEventError.EmptyField),
                    navigateUp = {},
                    action = {}
                )
            }
            onNodeWithTag("OneButtonDialog", true).assertIsDisplayed()
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenEventSavedThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.event_saved)
                EditEventScreen(
                    state = EditEventViewState(showSavedDialog = true),
                    navigateUp = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenPerformTextInputThenTheTitleTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember {
                    mutableStateOf(
                        EditEventViewState(placeholder = false, schedule = ScheduleModel())
                    )
                }
                EditEventScreen(
                    state = state,
                    navigateUp = {},
                    action = { action ->
                        if (action is EditEventAction.ChangeTitle) {
                            state.schedule?.let {
                                state = state.copy(schedule = it.copy(title = action.title))
                            }
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
                var state by remember {
                    mutableStateOf(
                        EditEventViewState(placeholder = false, schedule = ScheduleModel())
                    )
                }
                EditEventScreen(
                    state = state,
                    navigateUp = {},
                    action = { action ->
                        if (action is EditEventAction.ChangeDescription) {
                            state.schedule?.let {
                                state =
                                    state.copy(schedule = it.copy(description = action.description))
                            }
                        }
                    }
                )
            }
            onNodeWithTag("DescriptionTextField", true).onChildAt(1).performTextInput("a")
            onNodeWithTag("DescriptionTextField", true).onChildAt(1).assertTextEquals("a")
        }
    }

    @Test
    fun whenPerformTextInputThenTheFreeSpaceTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember {
                    mutableStateOf(
                        EditEventViewState(placeholder = false, schedule = ScheduleModel())
                    )
                }
                EditEventScreen(
                    state = state,
                    navigateUp = {},
                    action = { action ->
                        if (action is EditEventAction.ChangeFreeSpaces) {
                            state.schedule?.let {
                                state = state.copy(
                                    schedule = it.copy(freeSpaces = action.freeSpaces.toInt())
                                )
                            }
                        }
                    }
                )
            }
            onNodeWithTag("FreeSpaceTextField", true).onChildAt(1).performTextInput("2")
            onNodeWithTag("FreeSpaceTextField", true).onChildAt(1).equals(2)
        }
    }
}
