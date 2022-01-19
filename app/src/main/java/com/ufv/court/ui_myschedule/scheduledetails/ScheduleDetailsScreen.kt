package com.ufv.court.ui_myschedule.scheduledetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScheduleDetailsScreen(navigateUp: () -> Unit) {
    ScheduleDetailsScreen(hiltViewModel(), navigateUp)
}

@Composable
private fun ScheduleDetailsScreen(
    viewModel: ScheduleDetailsViewModel,
    navigateUp: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ScheduleDetailsViewState.Empty)

    ScheduleDetailsScreen(
        state = viewState,
        navigateUp = navigateUp
    ) { action ->
        viewModel.submitAction(action)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduleDetailsScreen(
    state: ScheduleDetailsViewState,
    navigateUp: () -> Unit,
    action: (ScheduleDetailsAction) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            ScheduleDetailsBottomSheet(scope, modalBottomSheetState, action)
        }
    ) {
        ScheduleDetailsScreen(
            state = state,
            navigateUp = navigateUp,
            showBottomSheet = { scope.launch { modalBottomSheetState.show() } },
            action = action
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduleDetailsBottomSheet(
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    action: (ScheduleDetailsAction) -> Unit
) {
    CustomBottomSheetContent(
        onHideBottomSheet = { scope.launch { modalBottomSheetState.hide() } }
    ) {
        HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
        CustomBottomSheetOption(
            textId = R.string.edit_event,
            optionDescription = R.string.edit_event,
            imageVector = Icons.Outlined.Edit
        ) {
            scope.launch { modalBottomSheetState.hide() }
//            action(FilesAction.CheckPermission(true))
        }
        HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
        CustomBottomSheetOption(
            textId = R.string.cancel_event,
            optionDescription = R.string.cancel_event,
            imageVector = Icons.Default.EventBusy
        ) {
            scope.launch { modalBottomSheetState.hide() }
            action(ScheduleDetailsAction.ChangeShowCancellationDialog(true))
        }
    }
}

@Composable
private fun ScheduleDetailsScreen(
    state: ScheduleDetailsViewState,
    navigateUp: () -> Unit,
    showBottomSheet: () -> Unit,
    action: (ScheduleDetailsAction) -> Unit
) {
    Scaffold(topBar = {
        ScheduleDetailsToolbar(navigateUp, showBottomSheet)
    }) {
        if (state.schedule == null) {
            CircularLoading()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = state.schedule.title)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "todo")
                //        val freeSpaceId by remember(scheduleModel.freeSpaces) {
//            if (scheduleModel.freeSpaces > "1") {
//                mutableStateOf(R.string.x_free_spaces)
//            } else {
//                mutableStateOf(R.string.x_free_space)
//            }
//        }
//        Text(
//            text = stringResource(id = freeSpaceId, scheduleModel.freeSpaces),
//            style = MaterialTheme.typography.button
//        )
            }
        }
    }
    ErrorTreatment(state.error) {
        action(ScheduleDetailsAction.CleanErrors)
    }
    ConfirmCancellationDialog(show = state.showCancellationDialog, action = action)
}

@Composable
private fun ConfirmCancellationDialog(show: Boolean, action: (ScheduleDetailsAction) -> Unit) {
    if (show) {
        TwoButtonsDialog(
            title = stringResource(id = R.string.cancel_event),
            message = stringResource(R.string.after_confirm_cant_go_back),
            leftButtonText = stringResource(id = R.string.cancel),
            rightButtonText = stringResource(id = R.string.confirm),
            onLeftButtonClick = {
                action(ScheduleDetailsAction.ChangeShowCancellationDialog(false))
            },
            onRightButtonClick = { action(ScheduleDetailsAction.ConfirmEventCancellation) },
            onDismiss = {
                action(ScheduleDetailsAction.ChangeShowCancellationDialog(false))
            }
        )
    }
}

@Composable
private fun ScheduleDetailsToolbar(navigateUp: () -> Unit, showBottomSheet: () -> Unit) {
    CustomToolbar(
        toolbarText = stringResource(id = R.string.scheduled),
        onLeftButtonClick = navigateUp,
        rightIcon = {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        },
        onRightButtonClick = showBottomSheet
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
