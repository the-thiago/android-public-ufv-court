package com.ufv.court.core.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.ufv.court.R
import org.junit.Rule
import org.junit.Test

class SingleChoiceDropDownTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenExpandIconIsClickedThenChangeIsExpandedLambdaIsCalled() {
        composeTestRule.run {
            setContent {
                var isExpanded by remember {
                    mutableStateOf(false)
                }
                SingleChoiceDropDown(
                    items = listOf("item0"),
                    selectedItem = "",
                    isExpanded = isExpanded,
                    changeIsExpanded = { isExpanded = !isExpanded },
                    onItemClick = {}
                )
            }
            onNodeWithTag("SingleChoiceDropDownExpandIcon", true).performClick()
            onNodeWithTag("SingleChoiceDropDownItem0", true).assertIsDisplayed()
            onNodeWithTag("SingleChoiceDropDownExpandIcon", true).performClick()
            onNodeWithTag("SingleChoiceDropDownItem0", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenSelectedItemIsEmptyThenIsDisplayedSelectText() {
        var selectText = ""
        composeTestRule.run {
            setContent {
                selectText = stringResource(id = R.string.select)
                SingleChoiceDropDown(
                    items = listOf(),
                    selectedItem = "",
                    isExpanded = false,
                    changeIsExpanded = {},
                    onItemClick = {}
                )
            }
            onNodeWithTag("SingleChoiceDropDownSelectedText", true).assertTextEquals(selectText)
        }
    }

    @Test
    fun whenSelectedItemIsNotEmptyThenIsDisplayedTheSelectedItem() {
        val selectedText = "My selected option"
        composeTestRule.run {
            setContent {
                SingleChoiceDropDown(
                    items = listOf(),
                    selectedItem = selectedText,
                    isExpanded = false,
                    changeIsExpanded = {},
                    onItemClick = {}
                )
            }
            onNodeWithTag("SingleChoiceDropDownSelectedText", true).assertTextEquals(selectedText)
        }
    }

    @Test
    fun whenIsNotExpandedThenNoItemExists() {
        composeTestRule.run {
            setContent {
                SingleChoiceDropDown(
                    items = listOf("item0", "item1"),
                    selectedItem = "",
                    isExpanded = false,
                    changeIsExpanded = {},
                    onItemClick = {}
                )
            }
            onNodeWithTag("SingleChoiceDropDownItem", true).assertDoesNotExist()
            onNodeWithTag("SingleChoiceDropDownItem0", true).assertDoesNotExist()
        }
    }

    @Test
    fun whenIsExpandedThenExistTheExpandedItemList() {
        val list = listOf("item0", "item1")
        composeTestRule.run {
            setContent {
                SingleChoiceDropDown(
                    items = list,
                    selectedItem = "",
                    isExpanded = true,
                    changeIsExpanded = {},
                    onItemClick = {}
                )
            }
            onNodeWithTag("SingleChoiceDropDownItem0", true).assertIsDisplayed()
            onNodeWithTag("SingleChoiceDropDownItemText0", true).assertTextEquals(list[0])
            onNodeWithTag("SingleChoiceDropDownItem1", true).assertIsDisplayed()
            onNodeWithTag("SingleChoiceDropDownItemText1", true).assertTextEquals(list[1])
        }
    }

    @Test
    fun whenItemListIsClickedThenTheSelectedItemChanges() {
        val list = listOf("item0", "item1")
        composeTestRule.run {
            setContent {
                var selectedItem by remember {
                    mutableStateOf("")
                }
                SingleChoiceDropDown(
                    items = list,
                    selectedItem = selectedItem,
                    isExpanded = true,
                    changeIsExpanded = {},
                    onItemClick = { selectedItem = it }
                )
            }
            onNodeWithTag("SingleChoiceDropDownItemRadioButton0", true).performClick()
            onNodeWithTag("SingleChoiceDropDownSelectedText", true).assertTextEquals(list[0])
            onNodeWithTag("SingleChoiceDropDownItemRadioButton1", true).performClick()
            onNodeWithTag("SingleChoiceDropDownSelectedText", true).assertTextEquals(list[1])
        }
    }

    @Test
    fun whenLabelIsNotEmptyThenLabelIsDisplayed() {
        composeTestRule.run {
            val text = "My Label"
            setContent {
                SingleChoiceDropDown(
                    label = text,
                    items = listOf(),
                    selectedItem = "",
                    isExpanded = false,
                    changeIsExpanded = {},
                    onItemClick = {}
                )
            }
            onNodeWithTag("LabelCustomTextField", true)
                .assertIsDisplayed()
                .assertTextEquals(text.uppercase())
        }
    }

    @Test
    fun whenLabelIsEmptyThenLabelDoesNotExist() {
        composeTestRule.run {
            setContent {
                SingleChoiceDropDown(
                    label = "",
                    items = listOf(),
                    selectedItem = "",
                    isExpanded = false,
                    changeIsExpanded = {},
                    onItemClick = {}
                )
            }
            onNodeWithTag("LabelCustomTextField", true).assertDoesNotExist()
        }
    }
}
