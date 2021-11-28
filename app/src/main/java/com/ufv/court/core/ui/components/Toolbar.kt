package com.ufv.court.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ufv.court.app.theme.Solitude

@Composable
fun CustomToolbar(
    modifier: Modifier = Modifier,
    toolbarText: String = "",
    elevation: Dp = 0.dp,
    verticalPadding: Dp = 16.dp,
    rightIcon: @Composable (() -> Unit)? = null,
    onRightButtonClick: () -> Unit = { },
    onLeftButtonClick: () -> Unit
) {
    Surface(modifier = modifier, elevation = elevation, color = MaterialTheme.colors.background) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding)
        ) {
            RoundedBackButton(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart),
                onClick = onLeftButtonClick
            )
            if (toolbarText.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .testTag("NozToolbarText"),
                    text = toolbarText,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5
                )
            }
            if (rightIcon != null) {
                IconButton(
                    onClick = onRightButtonClick,
                    Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                        .background(color = Solitude, shape = CircleShape)
                        .testTag("NozToolbarRightIcon")
                ) {
                    rightIcon()
                }
            }
        }
    }
}
