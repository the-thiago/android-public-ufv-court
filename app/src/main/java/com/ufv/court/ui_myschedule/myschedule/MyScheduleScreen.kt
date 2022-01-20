package com.ufv.court.ui_myschedule.myschedule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.app.theme.ShipCove
import com.ufv.court.app.theme.Solitude
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.HorizontalDivisor
import com.ufv.court.core.ui.components.OneButtonErrorDialog
import com.ufv.court.core.ui.components.TimeInMillisDateInfo

@Composable
fun MyScheduleScreen(openScheduleDetails: (String) -> Unit) {
    MyScheduleScreen(hiltViewModel(), openScheduleDetails)
}

@Composable
private fun MyScheduleScreen(
    viewModel: MyScheduleViewModel,
    openScheduleDetails: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = MyScheduleViewState.Empty)

    MyScheduleScreen(viewState, openScheduleDetails) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun MyScheduleScreen(
    state: MyScheduleViewState,
    openScheduleDetails: (String) -> Unit,
    action: (MyScheduleAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 56.dp) // toolbar height
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ExpandableSection(textRes = R.string.time_as_participant) {
            if (state.scheduledAsParticipant.isEmpty()) {
                NoScheduleHereText()
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                state.scheduledAsParticipant.forEach {
                    ScheduledItem(it) {
                        openScheduleDetails(it.id)
                    }
                }
            }
        }
        HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
        ExpandableSection(textRes = R.string.times_scheduled) {
            if (state.scheduled.isEmpty()) {
                NoScheduleHereText()
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                state.scheduled.forEach {
                    ScheduledItem(it) {
                        openScheduleDetails(it.id)
                    }
                }
            }
        }
    }
    ErrorTreatment(state.error) {
        action(MyScheduleAction.CleanErrors)
    }
}

@Composable
private fun ScheduledItem(scheduleModel: ScheduleModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Solitude, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = scheduleModel.title,
                style = MaterialTheme.typography.h6,
                color = BlackRock,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (scheduleModel.cancelled) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.EventBusy,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = scheduleModel.scheduleType,
                style = MaterialTheme.typography.button
            )
            TimeInMillisDateInfo(scheduleModel.timeInMillis) { day, month, year ->
                Text(
                    text = "${day}/${month}/${year}",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun NoScheduleHereText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        text = stringResource(R.string.no_schedule_here),
        style = MaterialTheme.typography.body2,
        color = ShipCove,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ErrorTreatment(error: Throwable?, onDismiss: () -> Unit) {
    error?.let {
        OneButtonErrorDialog(
            message = it.message ?: stringResource(id = R.string.unknown_error),
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun ExpandableSection(
    modifier: Modifier = Modifier,
    textRes: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    val arrowIconRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                text = stringResource(id = textRes),
                style = MaterialTheme.typography.h5
            )
            IconButton(
                modifier = Modifier.rotate(arrowIconRotation),
                onClick = { expanded = !expanded }
            ) {
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
            }
        }
        if (expanded) {
            content()
        }
    }
}
