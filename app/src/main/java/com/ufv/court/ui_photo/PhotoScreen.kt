package com.ufv.court.ui_photo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ufv.court.R
import com.ufv.court.app.theme.Solitude
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CustomToolbar

@Composable
fun PhotoScreen(navigateUp: () -> Unit) {
    PhotoScreen(viewModel = hiltViewModel(), navigateUp = navigateUp)
}

@Composable
private fun PhotoScreen(
    viewModel: PhotoViewModel,
    navigateUp: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PhotoViewState.Empty)

    PhotoScreen(
        state = viewState,
        navigateUp = navigateUp
    )
}

@Composable
private fun PhotoScreen(
    state: PhotoViewState,
    navigateUp: () -> Unit
) {
    var toolbarExpanded by remember { mutableStateOf(true) }
    val backGroundColor by animateColorAsState(
        targetValue = if (toolbarExpanded) Solitude else Color.Black,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )
    Box(modifier = Modifier.fillMaxSize()) {
        ZoomableImage(
            modifier = Modifier
                .fillMaxSize()
                .background(backGroundColor)
                .align(Alignment.Center),
            url = state.image
        ) {
            toolbarExpanded = !toolbarExpanded
        }
        ExpandableToolbar(
            modifier = Modifier.align(Alignment.TopCenter),
            toolbarExpanded = toolbarExpanded,
            navigateUp = navigateUp
        )
    }
}

@Composable
private fun ZoomableImage(
    modifier: Modifier = Modifier,
    url: String,
    onClick: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(targetValue = scale)
    val transformable = rememberTransformableState { zoomChange, _, _ ->
        val newScale = scale * zoomChange
        scale = when {
            newScale < 1f -> 1f
            newScale > 3f -> 3f
            else -> newScale
        }
    }
    Image(
        modifier = modifier
            .clipToBounds()
            .fillMaxSize()
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale > 2f) {
                            1f
                        } else {
                            3f
                        }
                    },
                    onTap = { onClick() }
                )
            }
            .transformable(transformable),
        painter = rememberImagePainter(
            data = url,
            builder = {
                placeholder(R.drawable.ic_image_placeholder)
                error(R.drawable.ic_image_placeholder)
            }
        ),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.Center
    )
}

@Composable
private fun ExpandableToolbar(
    modifier: Modifier = Modifier,
    toolbarExpanded: Boolean,
    navigateUp: () -> Unit
) {
    AnimatedVisibility(
        visible = toolbarExpanded,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = spring(
                stiffness = Spring.StiffnessMedium,
                visibilityThreshold = IntOffset.VisibilityThreshold
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = spring(
                stiffness = Spring.StiffnessMedium,
                visibilityThreshold = IntOffset.VisibilityThreshold
            )
        )
    ) {
        CustomToolbar(
            modifier = modifier,
            onLeftButtonClick = navigateUp
        )
    }
}
