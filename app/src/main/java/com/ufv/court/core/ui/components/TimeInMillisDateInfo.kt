package com.ufv.court.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.*

@Composable
fun TimeInMillisDateInfo(
    timeInMillis: Long,
    content: @Composable (String, String, String) -> Unit
) {
    val calendar by remember(timeInMillis) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        mutableStateOf(calendar)
    }
    val day by remember(calendar) { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    val month by remember(calendar) { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    val year by remember(calendar) { mutableStateOf(calendar.get(Calendar.YEAR)) }
    content(
        day.toString().padStart(2, '0'),
        month.toString().padStart(2, '0'),
        year.toString().padStart(2, '0')
    )
}
