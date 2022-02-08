package com.ufv.court.ui_myschedule.scheduledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.comments_service.domain.model.Comment
import com.ufv.court.core.comments_service.domain.usecases.GetCommentsUseCase
import com.ufv.court.core.comments_service.domain.usecases.SendCommentsUseCase
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduleUseCase
import com.ufv.court.core.schedule_service.domain.usecases.UpdateScheduleUseCase
import com.ufv.court.core.user_service.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val sendCommentsUseCase: SendCommentsUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ScheduleDetailsAction>()

    private val _state: MutableStateFlow<ScheduleDetailsViewState> =
        MutableStateFlow(ScheduleDetailsViewState.Empty)
    val state: StateFlow<ScheduleDetailsViewState> = _state

    private val scheduleId: String = savedStateHandle.get<String>("id") ?: ""
    private var deleteCommentIndex = 0

    init {
        _state.value = state.value.copy(scheduleId = scheduleId)
        handleActions()
        getComments()
    }

    private fun getComments() {
        viewModelScope.launch {
            val result = getCommentsUseCase(GetCommentsUseCase.Params(eventId = scheduleId))
            if (result is Result.Success) {
                _state.value = state.value.copy(eventComments = result.data)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun getScheduleAndUser() {
        viewModelScope.launch {
            val scheduleResult = getScheduleUseCase(GetScheduleUseCase.Params(id = scheduleId))
            if (scheduleResult is Result.Success) {
                _state.value = state.value.copy(schedule = scheduleResult.data)
            } else if (scheduleResult is Result.Error) {
                _state.value = state.value.copy(error = scheduleResult.exception)
            }
            val userResult = getCurrentUserUseCase(Unit)
            if (userResult is Result.Success) {
                _state.value = state.value.copy(user = userResult.data)
            } else if (userResult is Result.Error) {
                _state.value = state.value.copy(error = userResult.exception)
            }
            if (scheduleResult is Result.Success && userResult is Result.Success) {
                _state.value = state.value.copy(
                    isTheOwner = scheduleResult.data.ownerId == userResult.data.id,
                    isParticipating = scheduleResult.data.participantsId.contains(userResult.data.id)
                )
            }
            _state.value = state.value.copy(placeholder = false)
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ScheduleDetailsAction.CleanErrors -> {
                        _state.value = state.value.copy(error = null)
                    }
                    is ScheduleDetailsAction.ChangeShowCancellationDialog -> {
                        _state.value = state.value.copy(showCancellationDialog = action.show)
                    }
                    ScheduleDetailsAction.ConfirmEventCancellation -> cancelEvent()
                    is ScheduleDetailsAction.ChangeShowCancelledDialog -> {
                        _state.value = state.value.copy(showCancelledDialog = action.show)
                    }
                    ScheduleDetailsAction.ReloadData -> getScheduleAndUser()
                    ScheduleDetailsAction.ParticipateClick -> {
                        if (state.value.isParticipating) {
                            cancelParticipation()
                        } else {
                            participateClick()
                        }
                    }
                    is ScheduleDetailsAction.ChangeShowParticipatingDialog -> {
                        _state.value = state.value.copy(showParticipatingDialog = action.show)
                    }
                    is ScheduleDetailsAction.ChangeShowCancelParticipationDialog -> {
                        _state.value = state.value.copy(showCancelParticipationDialog = action.show)
                    }
                    is ScheduleDetailsAction.ChangeComment -> {
                        _state.value = state.value.copy(comment = action.comment)
                    }
                    ScheduleDetailsAction.SendComment -> sendComment()
                    is ScheduleDetailsAction.ChangeShowDeleteCommentDialog -> {
                        _state.value = state.value.copy(showDeleteCommentDialog = action.show)
                    }
                    is ScheduleDetailsAction.ChangeShowDeletedCommentDialog -> {
                        _state.value = state.value.copy(showDeletedCommentDialog = action.show)
                    }
                    is ScheduleDetailsAction.DeleteCommentClick -> {
                        deleteCommentIndex = action.commentIndex
                        _state.value = state.value.copy(showDeleteCommentDialog = true)
                    }
                    ScheduleDetailsAction.ConfirmDeleteComment -> deleteComment()
                    ScheduleDetailsAction.Refresh -> {
                        _state.value = state.value.copy(isRefreshing = true)
                        getComments()
                        getScheduleAndUser()
                        delay(1000L)
                        _state.value = state.value.copy(isRefreshing = false)
                    }
                }
            }
        }
    }

    private fun deleteComment() {
        viewModelScope.launch {
            _state.value = state.value.copy(showDeleteCommentDialog = false)
            val newComments = state.value.eventComments.comments.toMutableList()
            newComments.removeAt(deleteCommentIndex)
            val newEventComments = state.value.eventComments.copy(comments = newComments)
            val result = sendCommentsUseCase(
                SendCommentsUseCase.Params(
                    eventComments = newEventComments
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(
                    eventComments = newEventComments,
                    showDeletedCommentDialog = true
                )
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun sendComment() {
        viewModelScope.launch {
            val currentUser = state.value.user ?: return@launch
            _state.value = state.value.copy(isSendingComment = true)
            val commentUpdated = getCommentsUseCase(GetCommentsUseCase.Params(eventId = scheduleId))
            if (commentUpdated is Result.Success) {
                _state.value = state.value.copy(eventComments = commentUpdated.data)
            }
            val newComments = state.value.eventComments.comments.toMutableList()
            val newComment = Comment(
                userId = currentUser.id,
                userName = currentUser.name,
                userPhoto = currentUser.image,
                time = System.currentTimeMillis(),
                text = state.value.comment
            )
            newComments.add(0, newComment)
            val newEventComments = state.value.eventComments.copy(comments = newComments)
            val result = sendCommentsUseCase(
                SendCommentsUseCase.Params(
                    eventComments = newEventComments
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(
                    isSendingComment = false,
                    showCommentSent = true,
                    comment = "",
                    eventComments = newEventComments
                )
                delay(2000L)
                _state.value = state.value.copy(showCommentSent = false)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(isSendingComment = false, error = result.exception)
            }
        }
    }

    private fun cancelParticipation() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val userId = state.value.user?.id ?: ""
            val schedule = state.value.schedule ?: return@launch
            val newParticipants = schedule.participantsId.toMutableList()
            val newFreeSpaces = if (newParticipants.remove(userId)) {
                schedule.freeSpaces + 1
            } else {
                schedule.freeSpaces
            }
            val newSchedule = schedule.copy(
                participantsId = newParticipants,
                freeSpaces = newFreeSpaces,
                hasFreeSpace = newFreeSpaces > 0
            )
            val result = updateScheduleUseCase(
                UpdateScheduleUseCase.Params(
                    id = schedule.id,
                    newSchedule = newSchedule
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(
                    schedule = newSchedule,
                    showCancelParticipationDialog = true,
                    isParticipating = false
                )
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
            getScheduleAndUser()
            _state.value = state.value.copy(isLoading = false)
        }
    }

    private fun participateClick() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val userId = state.value.user?.id ?: ""
            val schedule = state.value.schedule ?: return@launch
            val newParticipants = schedule.participantsId.toMutableList()
            newParticipants.add(userId)
            val newFreeSpaces = schedule.freeSpaces - 1
            val newSchedule = schedule.copy(
                participantsId = newParticipants,
                freeSpaces = newFreeSpaces,
                hasFreeSpace = newFreeSpaces > 0
            )
            val result = updateScheduleUseCase(
                UpdateScheduleUseCase.Params(
                    id = schedule.id,
                    newSchedule = newSchedule
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(
                    schedule = newSchedule, showParticipatingDialog = true, isParticipating = true
                )
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
            getScheduleAndUser()
            _state.value = state.value.copy(isLoading = false)
        }
    }

    private fun cancelEvent() {
        viewModelScope.launch {
            val schedule = state.value.schedule ?: return@launch
            _state.value = state.value.copy(showCancellationDialog = false)
            val newSchedule = schedule.copy(cancelled = true)
            val result = updateScheduleUseCase(
                UpdateScheduleUseCase.Params(
                    id = schedule.id,
                    newSchedule = newSchedule
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(showCancelledDialog = true, schedule = newSchedule)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    fun submitAction(action: ScheduleDetailsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
