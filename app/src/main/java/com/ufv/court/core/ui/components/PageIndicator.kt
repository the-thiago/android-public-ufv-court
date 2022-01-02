package com.ufv.court.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun SimplePageIndicator(
    modifier: Modifier = Modifier,
    selectedPage: Int,
    amountOfPages: Int,
    unselectedColor: Color = Color.Gray,
    selectedColor: Color = MaterialTheme.colors.primary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        (0 until amountOfPages).forEach {
            Canvas(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(16.dp)
                    .testTag("PageIndicatorCircle$it")
            ) {
                drawCircle(color = if (it == selectedPage) selectedColor else unselectedColor)
            }
        }
    }
}
