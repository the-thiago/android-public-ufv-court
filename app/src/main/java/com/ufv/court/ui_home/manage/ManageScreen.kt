package com.ufv.court.ui_home.manage

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.app.theme.ShipCove
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CircularLoading
import com.ufv.court.core.ui.components.CustomTextField
import com.ufv.court.core.ui.components.CustomToolbar
import com.ufv.court.core.ui.components.ScheduledItem
import java.util.*

@Composable
fun ManageScreen(
    navigateUp: () -> Unit,
    openScheduleDetails: (String) -> Unit
) {
    ManageScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openScheduleDetails = openScheduleDetails
    )
}

@Composable
private fun ManageScreen(
    viewModel: ManageViewModel,
    navigateUp: () -> Unit,
    openScheduleDetails: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ManageViewState.Empty)

    ManageScreen(
        state = viewState,
        navigateUp = navigateUp,
        openScheduleDetails = openScheduleDetails
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun ManageScreen(
    state: ManageViewState,
    navigateUp: () -> Unit,
    openScheduleDetails: (String) -> Unit,
    action: (ManageAction) -> Unit
) {
    Scaffold(topBar = {
        CustomToolbar(toolbarText = stringResource(id = R.string.times), elevation = 4.dp) {
            navigateUp()
        }
    }) {
        if (state.placeholder) {
            CircularLoading()
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        DatePicker(
                            label = stringResource(R.string.search_by_date),
                            date = state.selectedDate
                        ) {
                            action(ManageAction.ChangeSelectedDate(it))
                        }
                    }
                    if (state.selectedDate.isBlank()) {
                        items(items = state.allSchedules) {
                            ScheduledItem(scheduleModel = it) {
                                openScheduleDetails(it.id)
                            }
                        }
                    } else {
                        items(items = state.schedulesByDate) {
                            ScheduledItem(scheduleModel = it) {
                                openScheduleDetails(it.id)
                            }
                        }
                    }
                }
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                if (state.selectedDate.isNotBlank() && state.schedulesByDate.isEmpty()
                    && !state.isLoading
                ) {
                    NothingFoundText(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun NothingFoundText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.no_schedule_here),
        style = MaterialTheme.typography.body2,
        color = ShipCove,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun DatePicker(
    label: String,
    date: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val datePickerDialog = remember {
        createDatePickerDialog(context) { date ->
            onDateSelected(date)
        }
    }
    CustomTextField(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { datePickerDialog.show() }
        ),
        text = date,
        enabled = false,
        labelText = label,
        onTextChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = ShipCove
            )
        },
        trailingIcon = {
            IconButton(onClick = { onDateSelected("") }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = ShipCove
                )
            }
        }
    )
}

private fun createDatePickerDialog(
    context: Context,
    onDateSelected: (String) -> Unit
): DatePickerDialog {
    val calendar = Calendar.getInstance()
    return DatePickerDialog(
        context,
        R.style.PickerTheme,
        { _, year, month, dayOfMonth ->
            val date = String.format(
                Locale.getDefault(),
                "%02d/%02d/%04d",
                dayOfMonth,
                month + 1,
                year
            )
            onDateSelected(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}
