package com.ufv.court.ui_home.schedule

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.app.theme.DarkGreen
import com.ufv.court.app.theme.Manatee
import com.ufv.court.app.theme.Solitude
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi

data class Schedule(
    val hourStart: Int,
    val hourEnd: Int,
    val isScheduled: Boolean,
    val selected: Boolean
)

@Composable
fun ScheduleScreen(navigateUp: () -> Unit, openHome: () -> Unit) {
    ScheduleScreen(hiltViewModel(), navigateUp, openHome)
}

@Composable
private fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    navigateUp: () -> Unit,
    openHome: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ScheduleViewState.Empty)

    ScheduleScreen(viewState, navigateUp, openHome) { action ->
        viewModel.submitAction(action)
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun ScheduleScreen(
    state: ScheduleViewState,
    navigateUp: () -> Unit,
    openHome: () -> Unit,
    action: (ScheduleAction) -> Unit
) {
    Scaffold(
        topBar = {
            CustomToolbar(toolbarText = state.date, elevation = 4.dp) {
                navigateUp()
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                SelectTime(state.schedules, action)
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomTextField(
                        text = state.title,
                        labelText = stringResource(R.string.title),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    ) {
                        action(ScheduleAction.ChangeTitle(it))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(
                        text = state.description,
                        labelText = stringResource(R.string.description_optional),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    ) {
                        action(ScheduleAction.ChangeDescription(it))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    var typeIsExpanded by remember { mutableStateOf(false) }
                    SingleChoiceDropDown(
                        items = stringArrayResource(id = R.array.ScheduleTypes).toList(),
                        selectedItem = state.scheduleType,
                        isExpanded = typeIsExpanded,
                        changeIsExpanded = { typeIsExpanded = !typeIsExpanded },
                        onItemClick = { action(ScheduleAction.ChangeScheduleType(it)) },
                        label = stringResource(R.string.event_type)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = stringResource(R.string.there_are_free_spaces),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Checkbox(
                            checked = state.hasFreeSpace,
                            onCheckedChange = { action(ScheduleAction.ChangeHasFreeSpace(it)) },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
                        )
                    }
                    if (state.hasFreeSpace) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(
                            text = state.freeSpaces,
                            labelText = stringResource(R.string.free_spaces),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        ) {
                            if (it.isDigitsOnly() && it.length < 3) {
                                action(ScheduleAction.ChangeFreeSpace(it))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    LoadingButton(
                        isLoading = state.isLoading,
                        buttonText = stringResource(R.string.schedule_text),
                        onButtonClick = { action(ScheduleAction.CreateScheduleClick) }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
    ErrorTreatment(state.error) {
        action(ScheduleAction.CleanErrors)
    }
    ScheduledDialog(show = state.showScheduledDialog, date = state.date, onDismiss = openHome)
}

@Composable
private fun ScheduledDialog(show: Boolean, date: String, onDismiss: () -> Unit) {
    if (show) {
        OneButtonSuccessDialog(message = stringResource(R.string.scheduled_message, date)) {
            onDismiss()
        }
    }
}

@Composable
private fun ErrorTreatment(error: Throwable?, onDismiss: () -> Unit) {
    error?.let {
        when (it) {
            ScheduleError.EmptyField -> {
                OneButtonErrorDialog(message = stringResource(id = R.string.empty_field_error)) {
                    onDismiss()
                }
            }
            ScheduleError.UnselectTimeField -> {
                OneButtonErrorDialog(message = stringResource(id = R.string.select_a_time_error)) {
                    onDismiss()
                }
            }
        }
    }
}

@Composable
private fun SelectTime(
    schedules: List<Schedule>,
    action: (ScheduleAction) -> Unit
) {
    var schedulesIsExpanded by rememberSaveable { mutableStateOf(false) }
    val arrowIconRotation by animateFloatAsState(targetValue = if (schedulesIsExpanded) 180f else 0f)
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Solitude, RoundedCornerShape(16.dp))
            .animateContentSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { schedulesIsExpanded = !schedulesIsExpanded }
            )
            .padding(top = 16.dp, bottom = if (schedulesIsExpanded) 8.dp else 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                text = stringResource(id = R.string.select_a_time),
                style = MaterialTheme.typography.h5
            )
            IconButton(
                modifier = Modifier.rotate(arrowIconRotation),
                onClick = { schedulesIsExpanded = !schedulesIsExpanded }
            ) {
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
            }
        }
        if (schedulesIsExpanded) {
            Spacer(modifier = Modifier.height(16.dp))
            schedules.forEachIndexed { index, schedule ->
                ScheduleItem(schedule) {
                    action(ScheduleAction.ScheduleTimeClick(index))
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun ScheduleItem(schedule: Schedule, onClick: () -> Unit) {
    val color by remember(schedule.isScheduled) {
        if (schedule.isScheduled) {
            mutableStateOf(Manatee)
        } else {
            mutableStateOf(BlackRock)
        }
    }
    HorizontalDivisor(Modifier.padding(horizontal = 16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (schedule.selected) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = DarkGreen
                )
            } else {
                Spacer(modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(
                    id = R.string.schedule_hours,
                    schedule.hourStart.toString().padStart(2, '0'),
                    schedule.hourEnd.toString().padStart(2, '0'),
                ),
                style = MaterialTheme.typography.caption,
                color = color
            )
        }
        Row {
            if (schedule.isScheduled) {
                Text(
                    text = stringResource(R.string.Scheduled),
                    color = color,
                    style = MaterialTheme.typography.subtitle2
                )
            } else {
                Text(
                    text = stringResource(R.string.Available),
                    color = DarkGreen,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}
