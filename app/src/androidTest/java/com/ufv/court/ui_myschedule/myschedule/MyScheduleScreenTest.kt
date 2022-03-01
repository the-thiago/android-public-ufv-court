package com.ufv.court.ui_myschedule.myschedule

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.R
import org.junit.Rule
import org.junit.Test

class MyScheduleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenIsLoadingThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                MyScheduleScreen(
                    state = MyScheduleViewState(isLoading = true),
                    openScheduleDetails = {},
                    action = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenIsNotLoadingThenCircularLoadingDoesNotExist() {
        composeTestRule.run {
            setContent {
                MyScheduleScreen(
                    state = MyScheduleViewState(isLoading = false),
                    openScheduleDetails = {},
                    action = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenMyScheduleIsCreatedThenEventAsParticipantTextIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.events_as_participant)
                MyScheduleScreen(
                    state = MyScheduleViewState(isLoading = false),
                    openScheduleDetails = {},
                    action = {}
                )
            }
            onNodeWithTag("EventAsParticipantText", true)
                .onChildAt(0).onChildAt(0).assertTextEquals(message)
        }
    }

    @Test
    fun whenMyScheduleIsCreatedThenEventsScheduledTextIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.events_scheduled)
                MyScheduleScreen(
                    state = MyScheduleViewState(isLoading = false),
                    openScheduleDetails = {},
                    action = {}
                )
            }
            onNodeWithTag("EventsScheduledText", true)
                .onChildAt(0).onChildAt(0).assertTextEquals(message)
        }
    }

    @Test
    fun whenEventAsParticipantIsEmptyThenEmptyWarningIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.no_events_here)
                MyScheduleScreen(
                    state = MyScheduleViewState(isLoading = false),
                    openScheduleDetails = {},
                    action = {}
                )
            }
            onNodeWithTag("EventAsParticipantEmpty", true).assertTextEquals(message)
        }
    }

    @Test
    fun whenEventsScheduledIsEmptyThenEmptyWarningIsDisplayed() {
        var message = ""
        composeTestRule.run {
            setContent {
                message = stringResource(id = R.string.no_events_here)
                MyScheduleScreen(
                    state = MyScheduleViewState(isLoading = false),
                    openScheduleDetails = {},
                    action = {}
                )
            }
            onNodeWithTag("EventsScheduledEmpty", true).assertTextEquals(message)
        }
    }
}
