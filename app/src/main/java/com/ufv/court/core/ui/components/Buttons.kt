package com.ufv.court.core.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ufv.court.R
import com.ufv.court.app.theme.Solitude

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    expandedButtonWidth: Dp = 216.dp,
    isLoading: Boolean,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    val width by animateDpAsState(targetValue = if (isLoading) 46.dp else expandedButtonWidth)
    Button(
        modifier = modifier
            .width(width)
            .height(46.dp)
            .testTag("LoadingButton"),
        onClick = onButtonClick,
        shape = RoundedCornerShape(if (isLoading) 100.dp else 24.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            disabledBackgroundColor = MaterialTheme.colors.primary
        ),
        enabled = !isLoading,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(36.dp)
                    .testTag("ProgressIndicatorLoadingButton"),
                color = Color.White,
                strokeWidth = 3.dp
            )
        } else {
            Text(
                modifier = Modifier.testTag("TextLoadingButton"),
                text = buttonText.uppercase(),
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun RoundedBackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        modifier = modifier
            .background(color = Solitude, shape = CircleShape)
            .testTag("RoundedBackButton"),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = stringResource(R.string.Voltar),
            tint = MaterialTheme.colors.primary
        )
    }
}
