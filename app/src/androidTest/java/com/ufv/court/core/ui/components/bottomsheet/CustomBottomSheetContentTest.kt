package com.ufv.court.core.ui.components.bottomsheet

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.core.ui.components.CustomBottomSheetContent
import org.junit.Rule
import org.junit.Test

class CustomBottomSheetContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenBottomSheetContentIsCreatedThenLabelDisplayed() {
        composeTestRule.run {
            setContent {
                CustomBottomSheetContent(onHideBottomSheet = {}, options = {})
            }
            onNodeWithTag("SelectTextCustomBottomSheetContent", true)
                .assertIsDisplayed()
        }
    }

    @Test
    fun whenBottomSheetContentIsCreatedThenCloseIconIsDisplayed() {
        composeTestRule.run {
            setContent {
                CustomBottomSheetContent(onHideBottomSheet = {}, options = {})
            }
            onNodeWithTag("CloseIconCustomBottomSheetContent", true)
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }
}
