package com.ufv.court.ui_profile.editprofile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.MaskVisualTransformation
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

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
    if (state.placeholder) {
        CircularLoading()
    } else {
        Scaffold(topBar = {
            CustomToolbar(toolbarText = stringResource(R.string.edit_profile)) {
                navigateUp()
            }
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EditProfileImage(state = state, action = action)
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(text = state.name, labelText = stringResource(id = R.string.name)) {
                    action(EditProfileAction.ChangeName(it))
                }
                Spacer(modifier = Modifier.height(16.dp))
                val phoneVisualTransformation = remember {
                    MaskVisualTransformation(EditProfileViewModel.PHONE_MASK)
                }
                CustomTextField(
                    labelText = stringResource(R.string.phone_optional),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    text = state.phone,
                    visualTransformation = phoneVisualTransformation
                ) { phone ->
                    if (phone.length <= EditProfileViewModel.PHONE_LENGTH && phone.isDigitsOnly()) {
                        action(EditProfileAction.ChangePhone(phone))
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                LoadingButton(
                    isLoading = state.isLoading,
                    buttonText = stringResource(id = R.string.save)
                ) {
                    action(EditProfileAction.SaveProfile)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    ErrorTreatment(error = state.error, navigateUp = navigateUp) {
        action(EditProfileAction.CleanErrors)
    }
    ProfileEditedDialog(show = state.profileEditedDialog, navigateUp = navigateUp)
}

@Composable
private fun ProfileEditedDialog(show: Boolean, navigateUp: () -> Unit) {
    if (show) {
        OneButtonSuccessDialog(message = stringResource(id = R.string.profile_saved)) {
            navigateUp()
        }
    }
}

@Composable
private fun EditProfileImage(
    state: EditProfileViewState,
    action: (EditProfileAction) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) action(EditProfileAction.ChangeImageUri(uri))
        }
    )
    Box {
        TextFieldLabel(text = stringResource(R.string.photo))
        if (state.newImageUri == null) {
            RoundedImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
                    .align(Alignment.Center),
                image = state.currentImage
            )
        } else {
            PreviewUriImage(
                uri = state.newImageUri,
                onClick = { launcher.launch("image/*") }
            )
        }
    }
}

@Composable
private fun ErrorTreatment(error: Throwable?, navigateUp: () -> Unit, onDismiss: () -> Unit) {
    error?.let {
        when (it) {
            EditProfileError.EmptyField -> OneButtonErrorDialog(
                message = stringResource(id = R.string.empty_field_error),
                onDismiss = onDismiss
            )
            EditProfileError.InvalidPhone -> OneButtonErrorDialog(
                message = stringResource(id = R.string.invalid_phone),
                onDismiss = onDismiss
            )
            else -> OneButtonErrorDialog(
                message = it.message ?: stringResource(id = R.string.unknown_error),
                onDismiss = {
                    onDismiss()
                    navigateUp()
                }
            )
        }
    }
}
