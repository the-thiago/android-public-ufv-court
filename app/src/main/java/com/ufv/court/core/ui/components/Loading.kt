package com.ufv.court.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun CircularLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("CircularLoading"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
