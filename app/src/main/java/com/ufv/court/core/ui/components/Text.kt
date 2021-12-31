package com.ufv.court.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldLabel(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text.uppercase(),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp, start = 4.dp)
            .testTag("LabelCustomTextField"),
        textAlign = TextAlign.Start,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}