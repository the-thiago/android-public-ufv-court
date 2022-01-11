package com.ufv.court.ui_home.calendar.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.ui_home.calendar.CalendarDay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RowScope.DayItem(day: () -> CalendarDay, onClick: () -> Unit) {
    DayContainer(onClick) {
        DayNumberText({ day().selected }, { day().number })
        DayTypeCircle(day)
    }
}

@Composable
private fun DayNumberText(selected: () -> Boolean, number: () -> String) {
    val primaryColor = MaterialTheme.colors.primary
    val color by remember(selected()) {
        mutableStateOf(if (selected()) primaryColor else BlackRock)
    }
    Text(
        text = number(),
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.Normal,
        color = color
    )
}

@Composable
private fun DayTypeCircle(day: () -> CalendarDay) {
    when {
        day().hasEvent -> EventCircle(color = MaterialTheme.colors.primary)
        else -> Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
private fun EventCircle(modifier: Modifier = Modifier, color: Color, size: Dp = 8.dp) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        drawCircle(color = color)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RowScope.DayContainer(
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .dayContainer { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

private fun Modifier.dayContainer(onClick: () -> Unit): Modifier = composed {
    this.then(
        Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(vertical = 9.dp)
    )
}
