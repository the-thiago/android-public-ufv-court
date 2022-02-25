package com.ufv.court.core.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class CustomTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLabelIsEmptyThenLabelIsNotDisplayed() {
        composeTestRule.run {
            setContent {
                CustomTextField(text = "", onTextChange = {}, labelText = "")
            }
            onNodeWithTag("LabelCustomTextField", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenLabelIsNotEmptyThenLabelInUppercaseIsDisplayed() {
        val labelText = "My Label"
        composeTestRule.run {
            setContent {
                CustomTextField(text = "", onTextChange = {}, labelText = labelText)
            }
            onNodeWithTag("LabelCustomTextField", true)
                .assertIsDisplayed()
                .assertTextEquals(labelText.uppercase())
        }
    }

    @Test
    fun whenPerformTextInputThenTheTextFieldValueChanges() {
        val inputString = "My input"
        composeTestRule.run {
            setContent {
                var text by remember {
                    mutableStateOf("")
                }
                CustomTextField(text = text, onTextChange = { text = it })
            }
            onNodeWithTag("TextFieldCustomTextField", true).performTextInput(inputString)
            onNodeWithTag("TextFieldCustomTextField", true).assertTextEquals(inputString)
        }
    }
}
