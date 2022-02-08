package com.ufv.court.ui_home.manage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CustomToolbar

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
        CustomToolbar(toolbarText = stringResource(id = R.string.times)) {
            navigateUp()
        }
    }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(text = "Manage")
            }
        }
    }
}
