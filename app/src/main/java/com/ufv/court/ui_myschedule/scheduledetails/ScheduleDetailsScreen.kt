package com.ufv.court.ui_myschedule.scheduledetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CircularLoading
import com.ufv.court.core.ui.components.CustomToolbar
import com.ufv.court.core.ui.components.OneButtonErrorDialog

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

@Composable
private fun ScheduleDetailsScreen(
    state: ScheduleDetailsViewState,
    navigateUp: () -> Unit,
    action: (ScheduleDetailsAction) -> Unit
) {
    Scaffold(topBar = {
        CustomToolbar(toolbarText = stringResource(id = R.string.scheduled)) {
            navigateUp()
        }
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
