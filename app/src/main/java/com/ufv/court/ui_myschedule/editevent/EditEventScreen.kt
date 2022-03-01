package com.ufv.court.ui_myschedule.editevent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

@Composable
fun EditEventScreen(navigateUp: () -> Unit) {
    EditEventScreen(hiltViewModel(), navigateUp)
}

@Composable
private fun EditEventScreen(
    viewModel: EditEventViewModel,
    navigateUp: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = EditEventViewState.Empty)

    EditEventScreen(
        viewState,
        navigateUp
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
fun EditEventScreen(
    state: EditEventViewState,
    navigateUp: () -> Unit,
    action: (EditEventAction) -> Unit
) {
    Scaffold(topBar = {
        CustomToolbar(
            toolbarText = stringResource(id = R.string.editing_event),
            onLeftButtonClick = navigateUp,
            elevation = 4.dp
        )
    }) {
        if (state.placeholder || state.schedule == null) {
            CircularLoading()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextField(
                    modifier = Modifier.testTag("TitleTextField"),
                    text = state.schedule.title,
                    labelText = stringResource(id = R.string.title)
                ) {
                    action(EditEventAction.ChangeTitle(it))
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    modifier = Modifier.testTag("DescriptionTextField"),
                    text = state.schedule.description,
                    labelText = stringResource(id = R.string.description_optional),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    maxLines = 10
                ) {
                    action(EditEventAction.ChangeDescription(it))
                }
                Spacer(modifier = Modifier.height(16.dp))
                var typeIsExpanded by remember { mutableStateOf(false) }
                SingleChoiceDropDown(
                    items = stringArrayResource(id = R.array.ScheduleTypes).toList(),
                    selectedItem = state.schedule.scheduleType,
                    isExpanded = typeIsExpanded,
                    changeIsExpanded = { typeIsExpanded = !typeIsExpanded },
                    onItemClick = { action(EditEventAction.ChangeEventType(it)) },
                    label = stringResource(R.string.event_type)
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    modifier = Modifier.testTag("FreeSpaceTextField"),
                    text = state.remainingFreeSpaces,
                    labelText = stringResource(id = R.string.remaining_free_spaces),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ) {
                    if (it.isDigitsOnly() && it.length < 3) {
                        action(EditEventAction.ChangeFreeSpaces(it))
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                LoadingButton(
                    isLoading = state.isLoading,
                    buttonText = stringResource(R.string.save)
                ) {
                    action(EditEventAction.UpdateEventClick)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    ErrorTreatment(state.error) {
        action(EditEventAction.CleanErrors)
    }
    SavedDialog(show = state.showSavedDialog, navigateUp = navigateUp)
}

@Composable
private fun SavedDialog(show: Boolean, navigateUp: () -> Unit) {
    if (show) {
        OneButtonSuccessDialog(message = stringResource(R.string.event_saved)) {
            navigateUp()
        }
    }
}

@Composable
private fun ErrorTreatment(error: Throwable?, onDismiss: () -> Unit) {
    error?.let {
        when (it) {
            EditEventError.EmptyField -> OneButtonErrorDialog(
                message = stringResource(id = R.string.empty_field_error),
                onDismiss = onDismiss
            )
            else -> OneButtonErrorDialog(
                message = it.message ?: stringResource(id = R.string.unknown_error),
                onDismiss = onDismiss
            )
        }
    }
}
