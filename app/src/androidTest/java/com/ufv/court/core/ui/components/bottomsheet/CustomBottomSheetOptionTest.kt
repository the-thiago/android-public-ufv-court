package com.ufv.court.core.ui.components.bottomsheet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ufv.court.R
import com.ufv.court.core.ui.components.CustomBottomSheetOption
import org.junit.Rule
import org.junit.Test

class CustomBottomSheetOptionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenBottomSheetOptionIsCreatedThenLabelDisplayed() {
        var text = ""
        composeTestRule.run {
            setContent {
                text = stringResource(id = R.string.cancel)
                CustomBottomSheetOption(
                    textId = R.string.cancel,
                    optionDescription = R.string.description_optional,
                    imageVector = Icons.Default.Person,
                    onClick = {}
                )
            }
            onNodeWithTag("LabelCustomBottomSheetOption", true)
                .assertIsDisplayed()
                .assertTextEquals(text)
        }
    }

    @Test
    fun whenBottomSheetOptionHasVectorThenVectorIsDisplayed() {
        composeTestRule.run {
            setContent {
                CustomBottomSheetOption(
                    textId = R.string.cancel,
                    optionDescription = R.string.description_optional,
                    imageVector = Icons.Default.Person,
                    onClick = {}
                )
            }
            onNodeWithTag("VectorIconCustomBottomSheetOption", true)
                .assertIsDisplayed()
            onNodeWithTag("DrawableIconCustomBottomSheetOption", true)
                .assertDoesNotExist()
        }
    }

    @Test
    fun whenBottomSheetOptionHasDrawableThenDrawableIsDisplayed() {
        composeTestRule.run {
            setContent {
                CustomBottomSheetOption(
                    textId = R.string.cancel,
                    optionDescription = R.string.description_optional,
                    imageVector = Icons.Default.Person,
                    iconId = R.drawable.ic_back_arrow,
                    onClick = {}
                )
            }
            onNodeWithTag("DrawableIconCustomBottomSheetOption", true)
                .assertIsDisplayed()
            onNodeWithTag("VectorIconCustomBottomSheetOption", true)
                .assertDoesNotExist()
        }
    }
}
