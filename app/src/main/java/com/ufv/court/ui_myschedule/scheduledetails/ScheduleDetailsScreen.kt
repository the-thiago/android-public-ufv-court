package com.ufv.court.ui_myschedule.scheduledetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ufv.court.R
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*
import com.ufv.court.ui_myschedule.scheduledetails.components.CommentsSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScheduleDetailsScreen(
    navigateUp: () -> Unit,
    openEditEvent: (String) -> Unit,
    openParticipants: (String) -> Unit
) {
    ScheduleDetailsScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openEditEvent = openEditEvent,
        openParticipants = openParticipants
    )
}

@Composable
private fun ScheduleDetailsScreen(
    viewModel: ScheduleDetailsViewModel,
    navigateUp: () -> Unit,
    openEditEvent: (String) -> Unit,
    openParticipants: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ScheduleDetailsViewState.Empty)

    ScheduleDetailsScreen(
        state = viewState,
        navigateUp = navigateUp,
        openEditEvent = openEditEvent,
        openParticipants = openParticipants
    ) { action ->
        viewModel.submitAction(action)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduleDetailsScreen(
    state: ScheduleDetailsViewState,
    navigateUp: () -> Unit,
    openEditEvent: (String) -> Unit,
    openParticipants: (String) -> Unit,
    action: (ScheduleDetailsAction) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            ScheduleDetailsBottomSheet(
                scope = scope,
                modalBottomSheetState = modalBottomSheetState,
                action = action,
                openEditEvent = { openEditEvent(state.scheduleId) }
            )
        }
    ) {
        ScheduleDetailsScreen(
            state = state,
            navigateUp = navigateUp,
            showBottomSheet = { scope.launch { modalBottomSheetState.show() } },
            openParticipants = openParticipants,
            action = action
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduleDetailsBottomSheet(
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    action: (ScheduleDetailsAction) -> Unit,
    openEditEvent: () -> Unit
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
            openEditEvent()
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
    openParticipants: (String) -> Unit,
    action: (ScheduleDetailsAction) -> Unit
) {
    Scaffold(topBar = {
        ScheduleDetailsToolbar(
            navigateUp = navigateUp,
            showBottomSheet = showBottomSheet,
            isTheOwner = state.isTheOwner,
            cancelled = state.schedule?.cancelled ?: true
        )
    }) {
        if (state.placeholder || state.schedule == null) {
            CircularLoading()
        } else {
            SwipeRefresh(
                modifier = Modifier.fillMaxSize(),
                state = rememberSwipeRefreshState(state.isRefreshing),
                onRefresh = { action(ScheduleDetailsAction.Refresh) },
                indicator = { swipeState, trigger ->
                    SwipeRefreshIndicator(
                        state = swipeState,
                        refreshTriggerDistance = trigger,
                        contentColor = MaterialTheme.colors.primary
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        ScheduleInfo(state.schedule) {
                            openParticipants(state.scheduleId)
                        }
                        if (((!state.isTheOwner && state.schedule.hasFreeSpace) || state.isParticipating) &&
                            !state.schedule.cancelled
                        ) {
                            ParticipateButton(
                                isParticipating = state.isParticipating,
                                isLoading = state.isLoading,
                                action = action
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    Column {
                        CommentsSection(
                            currentUserId = state.user?.id ?: "",
                            isSendingComment = state.isSendingComment,
                            showCommentSent = state.showCommentSent,
                            eventComments = state.eventComments,
                            comment = state.comment,
                            action = action
                        )
                    }
                }
            }
        }
    }
    ErrorTreatment(state.error) {
        action(ScheduleDetailsAction.CleanErrors)
    }
    ConfirmCancelledDialog(show = state.showCancelledDialog, action = action)
    ConfirmCancellationDialog(show = state.showCancellationDialog, action = action)
    ParticipatingDialog(show = state.showParticipatingDialog, action = action)
    DeleteCommentDialog(show = state.showDeleteCommentDialog, action = action)
    DeletedCommentDialog(show = state.showDeletedCommentDialog, action = action)
    CancelParticipationDialog(show = state.showCancelParticipationDialog, action = action)
    OnLifecycleEvent(action = action)
}

@Composable
private fun ParticipateButton(
    isParticipating: Boolean,
    isLoading: Boolean,
    action: (ScheduleDetailsAction) -> Unit
) {
    Spacer(modifier = Modifier.height(32.dp))
    val buttonText by remember(isParticipating) {
        if (isParticipating) {
            mutableStateOf(R.string.cancel_participate)
        } else {
            mutableStateOf(R.string.participate)
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingButton(
            isLoading = isLoading,
            buttonText = stringResource(buttonText)
        ) {
            action(ScheduleDetailsAction.ParticipateClick)
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun ScheduleInfo(schedule: ScheduleModel, openParticipants: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = schedule.title,
            style = MaterialTheme.typography.h5,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        if (schedule.cancelled) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EventBusy,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.event_cancelled),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimeInMillisDateInfo(schedule.timeInMillis) { day, month, year ->
                Text(
                    text = "${day}/${month}/${year}",
                    style = MaterialTheme.typography.button
                )
            }
            Text(
                text = stringResource(
                    id = R.string.schedule_hours,
                    schedule.hourStart.toString().padStart(2, '0'),
                    schedule.hourEnd.toString().padStart(2, '0'),
                ),
                style = MaterialTheme.typography.button
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val freeSpaceId by remember(schedule.freeSpaces) {
            if (schedule.freeSpaces > 1) {
                mutableStateOf(R.string.x_free_spaces)
            } else {
                mutableStateOf(R.string.x_free_space)
            }
        }
        Text(
            text = "${schedule.scheduleType} - ${
                stringResource(id = freeSpaceId, schedule.freeSpaces)
            }",
            style = MaterialTheme.typography.button
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (schedule.description.isBlank()) {
                stringResource(id = R.string.there_is_no_description)
            } else schedule.description,
            style = MaterialTheme.typography.body1
        )
        if (schedule.participantsId.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = openParticipants, shape = RoundedCornerShape(24.dp)) {
                    Text(
                        text = stringResource(R.string.see_participants),
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun OnLifecycleEvent(action: (ScheduleDetailsAction) -> Unit) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                action(ScheduleDetailsAction.ReloadData)
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

@Composable
private fun ConfirmCancelledDialog(show: Boolean, action: (ScheduleDetailsAction) -> Unit) {
    if (show) {
        OneButtonSuccessDialog(message = stringResource(R.string.event_cancelled)) {
            action(ScheduleDetailsAction.ChangeShowCancelledDialog(false))
        }
    }
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
private fun ParticipatingDialog(show: Boolean, action: (ScheduleDetailsAction) -> Unit) {
    if (show) {
        OneButtonSuccessDialog(message = stringResource(R.string.now_you_are_participating)) {
            action(ScheduleDetailsAction.ChangeShowParticipatingDialog(false))
        }
    }
}

@Composable
private fun DeleteCommentDialog(show: Boolean, action: (ScheduleDetailsAction) -> Unit) {
    if (show) {
        TwoButtonsDialog(
            title = stringResource(R.string.delete),
            message = stringResource(R.string.do_you_want_to_delete_comment),
            leftButtonText = stringResource(R.string.cancel),
            rightButtonText = stringResource(R.string.delete),
            onLeftButtonClick = {
                action(ScheduleDetailsAction.ChangeShowDeleteCommentDialog(false))
            },
            onRightButtonClick = { action(ScheduleDetailsAction.ConfirmDeleteComment) },
            onDismiss = {
                action(ScheduleDetailsAction.ChangeShowDeleteCommentDialog(false))
            }
        )
    }
}

@Composable
private fun DeletedCommentDialog(show: Boolean, action: (ScheduleDetailsAction) -> Unit) {
    if (show) {
        OneButtonSuccessDialog(message = stringResource(R.string.comment_deleted)) {
            action(ScheduleDetailsAction.ChangeShowDeletedCommentDialog(false))
        }
    }
}

@Composable
private fun CancelParticipationDialog(show: Boolean, action: (ScheduleDetailsAction) -> Unit) {
    if (show) {
        OneButtonSuccessDialog(message = stringResource(R.string.now_you_are_not_participating)) {
            action(ScheduleDetailsAction.ChangeShowCancelParticipationDialog(false))
        }
    }
}

@Composable
private fun ScheduleDetailsToolbar(
    navigateUp: () -> Unit,
    showBottomSheet: () -> Unit,
    isTheOwner: Boolean,
    cancelled: Boolean
) {
    CustomToolbar(
        elevation = 4.dp,
        toolbarText = stringResource(id = R.string.event),
        onLeftButtonClick = navigateUp,
        rightIcon = if (isTheOwner && !cancelled) {
            {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        } else null,
        onRightButtonClick = if (isTheOwner && !cancelled) {
            showBottomSheet
        } else {
            {}
        }
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
