package com.ufv.court.ui_myschedule.scheduledetails

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

class ScheduleDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenPlaceHolderIsTrueThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = true,
                        schedule = ScheduleModel()
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
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
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel()
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenScheduleDetailsIsCreatedThenEventInfoIsDisplayed() {
        val title = "test"
        composeTestRule.run {
            setContent {
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel(title = title)
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("TitleScheduleInfo", true).assertTextEquals(title)
        }
    }

    @Test
    fun whenScheduleIsCancelledThenCancelledInfoIsDisplayed() {
        composeTestRule.run {
            setContent {
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel(cancelled = true)
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("CancelledScheduleInfo", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenScheduleIsNotCancelledThenCancelledInfoDoesNotExist() {
        composeTestRule.run {
            setContent {
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel(cancelled = false)
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("CancelledScheduleInfo", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenScheduleDescriptionIsEmptyThenNoDescriptionWarningIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.there_is_no_description)
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel(description = "")
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("DescriptionScheduleInfo", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenScheduleDescriptionIsNotEmptyThenDescriptionIsDisplayed() {
        val description = "test"
        composeTestRule.run {
            setContent {
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel(description = description)
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("DescriptionScheduleInfo", true).assertTextEquals(description)
        }
    }

    @Test
    fun whenScheduleHasNoParticipantsThenParticipantsButtonDoesNotExist() {
        composeTestRule.run {
            setContent {
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel(participantsId = emptyList())
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("ParticipantsScheduleInfo", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenScheduleHasParticipantsThenParticipantsButtonIsDiplayed() {
        composeTestRule.run {
            setContent {
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        schedule = ScheduleModel(participantsId = listOf("id00"))
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("ParticipantsScheduleInfo", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenPerformTextInputThenTheTeamNameTextFieldValueChanges() {
        composeTestRule.run {
            setContent {
                var state by remember {
                    mutableStateOf(
                        ScheduleDetailsViewState(
                            placeholder = false,
                            schedule = ScheduleModel()
                        )
                    )
                }
                ScheduleDetailsScreen(
                    state = state,
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {
                        if (it is ScheduleDetailsAction.ChangeComment) {
                            state = state.copy(comment = it.comment)
                        }
                    }
                )
            }
            onNodeWithTag("CommentTextField", true).onChildAt(0).performTextInput("a")
            onNodeWithTag("CommentTextField", true).onChildAt(0).assertTextEquals("a")
        }
    }

    @Test
    fun whenShowCancellationDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.after_confirm_cant_go_back)
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        showCancellationDialog = true
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageTwoButtonsDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenShowCancelledDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.event_cancelled)
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        showCancelledDialog = true
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenShowParticipatingDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.now_you_are_participating)
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        showParticipatingDialog = true
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenShowCancelParticipationDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.now_you_are_not_participating)
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        showCancelParticipationDialog = true
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenShowDeleteCommentDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.do_you_want_to_delete_comment)
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        showDeleteCommentDialog = true
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageTwoButtonsDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun whenShowDeletedCommentDialogThenDialogIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.comment_deleted)
                ScheduleDetailsScreen(
                    state = ScheduleDetailsViewState(
                        placeholder = false,
                        showDeletedCommentDialog = true
                    ),
                    navigateUp = {},
                    showBottomSheet = {},
                    openParticipants = {},
                    action = {}
                )
            }
            onNodeWithTag("MessageOneButtonDialog", true).assertIsDisplayed()
                .assertTextEquals(message)
        }
    }
}
