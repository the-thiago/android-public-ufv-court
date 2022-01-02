package com.ufv.court.ui_login.forgotpassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

@Composable
fun ForgotPasswordScreen(navigateUp: () -> Unit) {
    ForgotPasswordScreen(viewModel = hiltViewModel(), navigateUp = navigateUp)
}

@Composable
private fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    navigateUp: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ForgotPasswordViewState.Empty)

    ForgotPasswordScreen(
        state = viewState,
        navigateUp = navigateUp
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun ForgotPasswordScreen(
    state: ForgotPasswordViewState,
    navigateUp: () -> Unit,
    action: (ForgotPasswordAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ForgotPasswordToolbar(navigateUp = navigateUp, action = action)
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = state.email,
            labelText = stringResource(id = R.string.email)
        ) {
            action(ForgotPasswordAction.ChangeEmail(it))
        }
        Spacer(modifier = Modifier.height(32.dp))
        LoadingButton(isLoading = state.isLoading, buttonText = stringResource(R.string.send)) {
            action(ForgotPasswordAction.SendEmail)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
    InfoDialog(show = state.showInfoDialog, action = action)
    EmailSentDialog(show = state.showEmailSentDialog, email = state.email) {
        action(ForgotPasswordAction.ChangeShowEmailSentDialog(false))
        navigateUp()
    }
    state.error?.let {
        ErrorTreatment(error = state.error) {
            action(ForgotPasswordAction.CleanErrors)
        }
    }
}

@Composable
private fun ErrorTreatment(error: Throwable, onDismiss: () -> Unit) {
    when (error) {
        is ForgotPasswordError.InvalidEmail -> OneButtonErrorDialog(
            message = stringResource(R.string.type_a_valid_email),
            onDismiss = onDismiss
        )
        is ForgotPasswordError.NoUserFound -> OneButtonErrorDialog(
            message = stringResource(R.string.email_not_registered),
            onDismiss = onDismiss
        )
        else -> OneButtonErrorDialog(
            message = stringResource(R.string.unknown_error),
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun ForgotPasswordToolbar(navigateUp: () -> Unit, action: (ForgotPasswordAction) -> Unit) {
    CustomToolbar(
        toolbarText = stringResource(R.string.reset_password),
        onLeftButtonClick = navigateUp,
        rightIcon = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        },
        onRightButtonClick = {
            action(ForgotPasswordAction.ChangeShowInfoDialog(true))
        }
    )
}

@Composable
private fun InfoDialog(show: Boolean, action: (ForgotPasswordAction) -> Unit) {
    if (show) {
        OneButtonSuccessDialog(
            title = stringResource(id = R.string.reset_password),
            message = stringResource(R.string.email_will_be_sent),
            onDismiss = { action(ForgotPasswordAction.ChangeShowInfoDialog(false)) }
        )
    }
}

@Composable
private fun EmailSentDialog(show: Boolean, email: String, onDismiss: () -> Unit) {
    if (show) {
        OneButtonSuccessDialog(
            message = stringResource(id = R.string.email_sent_to, email),
            onDismiss = onDismiss
        )
    }
}
