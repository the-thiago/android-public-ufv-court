package com.ufv.court.core.ui.components.dialogs

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.R
import com.ufv.court.core.ui.components.OneButtonDialog
import org.junit.Rule
import org.junit.Test

class OneButtonDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenDialogIsCreatedThenTheCorrectStringsAreDisplayedAndButtonHasClickAction() {
        val title = "title"
        val message = "message"
        val confirmText = "confirmText"
        composeTestRule.run {
            setContent {
                OneButtonDialog(
                    title = title,
                    message = message,
                    confirmText = confirmText,
                    iconResourceId = R.drawable.ic_check,
                    onDismiss = {}
                )
            }
            onNodeWithTag("TitleOneButtonDialog", true).assertTextEquals(title)
            onNodeWithTag("MessageOneButtonDialog", true).assertTextEquals(message)
            onNodeWithTag("ConfirmTextOneButtonDialog", true).assertTextEquals(confirmText)
            onNodeWithTag("ButtonOneButtonDialog", true).assertHasClickAction()
        }
    }
}
