package com.ufv.court.core.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import org.junit.Rule
import org.junit.Test

class ScheduledItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenScheduledItemIsCreatedThenTypeIsDisplayed() {
        val type = "Type"
        val item = ScheduleModel(scheduleType = type)
        composeTestRule.run {
            setContent {
                ScheduledItem(
                    scheduleModel = item,
                    onClick = {}
                )
            }
            onNodeWithTag("TypeScheduledItem", true)
                .assertIsDisplayed()
                .assertTextEquals(type)
        }
    }

    @Test
    fun whenScheduledItemIsCreatedThenDateIsDisplayed() {
        val item = ScheduleModel()
        composeTestRule.run {
            setContent {
                ScheduledItem(
                    scheduleModel = item,
                    onClick = {}
                )
            }
            onNodeWithTag("DateScheduledItem", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenScheduledIsCancelledThenCancelledIconIsDisplayed() {
        val item = ScheduleModel(cancelled = true)
        composeTestRule.run {
            setContent {
                ScheduledItem(
                    scheduleModel = item,
                    onClick = {}
                )
            }
            onNodeWithTag("CancelledScheduledItem", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenScheduledIsNotCancelledThenCancelledIconIsNotDisplayed() {
        val item = ScheduleModel(cancelled = false)
        composeTestRule.run {
            setContent {
                ScheduledItem(
                    scheduleModel = item,
                    onClick = {}
                )
            }
            onNodeWithTag("CancelledScheduledItem", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenScheduledItemIsCreatedThenTitleIsDisplayed() {
        val item = ScheduleModel()
        composeTestRule.run {
            setContent {
                ScheduledItem(
                    scheduleModel = item,
                    onClick = {}
                )
            }
            onNodeWithTag("TitleScheduledItem", true).assertIsDisplayed()
        }
    }
}
