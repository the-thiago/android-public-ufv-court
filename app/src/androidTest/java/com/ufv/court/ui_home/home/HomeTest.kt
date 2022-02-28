package com.ufv.court.ui_home.home

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class HomeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateThenCircularLoadingIsDisplayed() {
        composeTestRule.run {
            setContent {
                HomeScreen(HomeViewState(isLoading = true), {}, {}, {}, {})
            }
            onNodeWithTag("CircularLoading", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotLoadingStateThenCircularLoadingDoesNotExist() {
        composeTestRule.run {
            setContent {
                HomeScreen(HomeViewState(isLoading = false), {}, {}, {}, {})
            }
            onNodeWithTag("CircularLoading", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenHomeIsCreatedThenJoinEventTextIsDisplayed() {
        composeTestRule.run {
            setContent {
                HomeScreen(HomeViewState(), {}, {}, {}, {})
            }
            onNodeWithTag("JoinEventText", true).assertIsDisplayed()
        }
    }

    @Test
    fun whenUserIsAdminThenHeaderOpenManageIsDisplayed() {
        composeTestRule.run {
            setContent {
                HomeScreen(HomeViewState(isAdmin = true), {}, {}, {}, {})
            }
            onNodeWithTag("HeaderOpenManage", true).assertIsDisplayed().assertHasClickAction()
        }
    }

    @Test
    fun whenUserIsNotAdminThenHeaderOpenManageDoesNotExist() {
        composeTestRule.run {
            setContent {
                HomeScreen(HomeViewState(isAdmin = false), {}, {}, {}, {})
            }
            onNodeWithTag("HeaderOpenManage", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenHomeIsCreatedThenHeaderCreateEventIsDisplayed() {
        composeTestRule.run {
            setContent {
                HomeScreen(HomeViewState(), {}, {}, {}, {})
            }
            onNodeWithTag("HeaderCreateEvent", true).assertIsDisplayed().assertHasClickAction()
        }
    }
}
