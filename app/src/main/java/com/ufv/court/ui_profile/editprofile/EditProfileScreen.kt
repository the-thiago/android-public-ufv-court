package com.ufv.court.ui_profile.editprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun EditProfileScreen(navigateUp: () -> Unit) {
    EditProfileScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp
    )
}

@Composable
private fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    navigateUp: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = EditProfileViewState.Empty)

    EditProfileScreen(
        state = viewState,
        navigateUp = navigateUp
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun EditProfileScreen(
    state: EditProfileViewState,
    navigateUp: () -> Unit,
    action: (EditProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CustomToolbar(toolbarText = stringResource(R.string.edit_profile)) {
            navigateUp()
        }
        Text(text = "Editing profile")
    }
}
