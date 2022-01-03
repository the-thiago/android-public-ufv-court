package com.ufv.court.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ufv.court.R

@Composable
fun RoundedImage(url: String, size: Dp = 152.dp) {
    Image(
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        painter = rememberImagePainter(
            data = url,
            builder = {
                placeholder(R.drawable.user_photo_placeholder)
                error(R.drawable.user_photo_placeholder)
            }
        ),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}