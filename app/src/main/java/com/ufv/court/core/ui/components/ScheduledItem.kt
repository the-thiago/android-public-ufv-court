package com.ufv.court.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.app.theme.Solitude
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

@Composable
fun ScheduledItem(
    scheduleModel: ScheduleModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Solitude, shape = RoundedCornerShape(corner = CornerSize(16.dp)))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .testTag("TitleScheduledItem"),
                text = scheduleModel.title,
                style = MaterialTheme.typography.h6,
                color = BlackRock,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (scheduleModel.cancelled) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.testTag("CancelledScheduledItem"),
                    imageVector = Icons.Default.EventBusy,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TimeInMillisDateInfo(scheduleModel.timeInMillis) { day, month, year ->
                Text(
                    modifier = Modifier.testTag("DateScheduledItem"),
                    text = " ${day}/${month}/${year} - ${scheduleModel.hourStart}h as ${scheduleModel.hourEnd}h",
                    style = MaterialTheme.typography.button
                )
            }
            Text(
                modifier = Modifier.testTag("TypeScheduledItem"),
                text = scheduleModel.scheduleType,
                style = MaterialTheme.typography.button
            )
        }
    }
}
