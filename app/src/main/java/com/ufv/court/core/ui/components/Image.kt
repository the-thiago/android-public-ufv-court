package com.ufv.court.core.ui.components

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ufv.court.R

@Composable
fun RoundedImage(modifier: Modifier = Modifier, image: String, size: Dp = 152.dp) {
    Image(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        painter = rememberImagePainter(
            data = image,
            builder = {
                placeholder(R.drawable.user_photo_placeholder)
                error(R.drawable.user_photo_placeholder)
            }
        ),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun BoxScope.PreviewUriImage(uri: Uri, onClick: () -> Unit) {
    val context = LocalContext.current
    val bitmap by remember(uri) {
        if (Build.VERSION.SDK_INT < 28) {
            mutableStateOf(
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            )
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            mutableStateOf(ImageDecoder.decodeBitmap(source))
        }
    }
    Image(
        modifier = Modifier
            .roundedUserPhoto(onClick)
            .align(Alignment.Center),
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.colorMatrix(colorMatrix = ColorMatrix().apply {
            setToSaturation(.25F)
        })
    )
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = stringResource(id = R.string.change),
        color = Color.White,
        style = MaterialTheme.typography.h5
    )
}

fun Modifier.roundedUserPhoto(onClick: () -> Unit): Modifier = this
    .size(152.dp)
    .clip(CircleShape)
    .clickable { onClick() }