package com.ufv.court.ui_myschedule.myschedule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ufv.court.R
import com.ufv.court.app.theme.ShipCove
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CircularLoading
import com.ufv.court.core.ui.components.HorizontalDivisor
import com.ufv.court.core.ui.components.OneButtonErrorDialog
import com.ufv.court.core.ui.components.ScheduledItem

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
fun MyScheduleScreen(
    state: MyScheduleViewState,
    openScheduleDetails: (String) -> Unit,
    action: (MyScheduleAction) -> Unit
) {
    SwipeRefresh(
        modifier = Modifier
            .padding(bottom = 56.dp) // toolbar height
            .fillMaxSize(),
        state = rememberSwipeRefreshState(state.isRefreshing),
        onRefresh = { action(MyScheduleAction.Refresh) },
        indicator = { swipeState, trigger ->
            SwipeRefreshIndicator(
                state = swipeState,
                refreshTriggerDistance = trigger,
                contentColor = MaterialTheme.colors.primary
            )
        }
    ) {
        if (state.isLoading) {
            CircularLoading()
        } else {
            LoadedMyScheduleScreen(state, openScheduleDetails)
        }
    }
    ErrorTreatment(state.error) {
        action(MyScheduleAction.CleanErrors)
    }
}

@Composable
private fun LoadedMyScheduleScreen(
    state: MyScheduleViewState,
    openScheduleDetails: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ExpandableSection(
            modifier = Modifier.testTag("EventAsParticipantText"),
            textRes = R.string.events_as_participant
        ) {
            if (state.scheduledAsParticipant.isEmpty()) {
                NoScheduleHereText(modifier = Modifier.testTag("EventAsParticipantEmpty"))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    state.scheduledAsParticipant.forEach {
                        ScheduledItem(it) {
                            openScheduleDetails(it.id)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
        ExpandableSection(
            modifier = Modifier.testTag("EventsScheduledText"),
            textRes = R.string.events_scheduled
        ) {
            if (state.scheduled.isEmpty()) {
                NoScheduleHereText(modifier = Modifier.testTag("EventsScheduledEmpty"))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    state.scheduled.forEach {
                        ScheduledItem(it) {
                            openScheduleDetails(it.id)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun NoScheduleHereText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        text = stringResource(R.string.no_events_here),
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
