package com.ufv.court.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ufv.court.app.theme.LinkWater

@Composable
fun HorizontalDivisor(modifier: Modifier = Modifier, color: Color = LinkWater) {
    Spacer(
        modifier = modifier
            .height(0.5.dp)
            .fillMaxWidth()
            .background(color)
    )
}

@Composable
fun VerticalDivisor(modifier: Modifier = Modifier, color: Color = LinkWater) {
    Spacer(
        modifier = modifier
            .width(0.5.dp)
            .fillMaxHeight()
            .background(color)
    )
}
