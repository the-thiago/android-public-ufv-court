package com.ufv.court.ui_home.manage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
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
import com.ufv.court.core.ui.components.ScheduledItem

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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = state.schedules) {
                    ScheduledItem(scheduleModel = it) {
                        openScheduleDetails(it.id)
                    }
                }
            }
        }
    }
}
