package com.ufv.court.ui_myschedule.participants

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.R
import com.ufv.court.core.user_service.domain.model.UserModel
import org.junit.Rule
import org.junit.Test

class ParticipantsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenParticipantsIsNullThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                ParticipantsScreen(
                    state = ParticipantsViewState(participants = null),
                    openPhoto = {},
                    navigateUp = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenParticipantsIsNotNullThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                ParticipantsScreen(
                    state = ParticipantsViewState(participants = emptyList()),
                    openPhoto = {},
                    navigateUp = {}
                )
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenHasOneParticipantThenParticipantsTitleIsSingular() {
        val participants = listOf(UserModel())
        var text = ""
        composeTestRule.run {
            setContent {
                text = stringResource(id = R.string.participants_singular, participants.size)
                ParticipantsScreen(
                    state = ParticipantsViewState(participants = participants),
                    openPhoto = {},
                    navigateUp = {}
                )
            }
            onNodeWithTag("ParticipantsTitle", true).assertTextEquals(text)
        }
    }

    @Test
    fun whenHasMoreThanOneParticipantThenParticipantsTitleIsPlural() {
        val participants = listOf(UserModel(), UserModel())
        var text = ""
        composeTestRule.run {
            setContent {
                text = stringResource(id = R.string.participants_plural, participants.size)
                ParticipantsScreen(
                    state = ParticipantsViewState(participants = participants),
                    openPhoto = {},
                    navigateUp = {}
                )
            }
            onNodeWithTag("ParticipantsTitle", true).assertTextEquals(text)
        }
    }
}
