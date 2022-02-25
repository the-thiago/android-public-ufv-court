package com.ufv.court.core.ui.components.dialogs

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.core.ui.components.TwoButtonsDialog
import org.junit.Rule
import org.junit.Test

class TwoButtonsDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenDialogIsCreatedThenTheCorrectStringsAreDisplayedAndButtonHasClickAction() {
        val title = "title"
        val message = "message"
        val leftButtonText = "leftButtonText"
        val rightButtonText = "rightButtonText"
        composeTestRule.run {
            setContent {
                TwoButtonsDialog(
                    title = title,
                    message = message,
                    leftButtonText = leftButtonText,
                    rightButtonText = rightButtonText,
                    onLeftButtonClick = { },
                    onRightButtonClick = { },
                    onDismiss = { }
                )
            }
            onNodeWithTag("TitleTwoButtonsDialog", true).assertTextEquals(title)
            onNodeWithTag("MessageTwoButtonsDialog", true).assertTextEquals(message)
            onNodeWithTag("LeftButtonTwoButtonsDialog", true).assertHasClickAction()
            onNodeWithTag("RightButtonTwoButtonsDialog", true).assertHasClickAction()
            onNodeWithTag("LeftButtonTextTwoButtonsDialog", true).assertTextEquals(leftButtonText)
            onNodeWithTag("RightButtonTextTwoButtonsDialog", true).assertTextEquals(rightButtonText)
        }
    }
}
