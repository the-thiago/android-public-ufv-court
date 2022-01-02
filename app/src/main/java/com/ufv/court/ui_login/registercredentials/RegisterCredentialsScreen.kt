package com.ufv.court.ui_login.registercredentials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

@Composable
fun RegisterCredentialsScreen(
    navigateUp: () -> Unit,
    openLogin: () -> Unit
) {
    RegisterCredentialsScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openLogin = openLogin
    )
}

@Composable
private fun RegisterCredentialsScreen(
    viewModel: RegisterCredentialsViewModel,
    navigateUp: () -> Unit,
    openLogin: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = RegisterCredentialsViewState.Empty)

    RegisterCredentialsScreen(
        state = viewState,
        navigateUp = navigateUp,
        openLogin = openLogin
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun RegisterCredentialsScreen(
    state: RegisterCredentialsViewState,
    navigateUp: () -> Unit,
    openLogin: () -> Unit,
    action: (RegisterCredentialsAction) -> Unit
) {
    Scaffold(topBar = {
        CustomToolbar(
            toolbarText = stringResource(R.string.create_account),
            elevation = 4.dp,
            onLeftButtonClick = navigateUp
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                labelText = stringResource(R.string.email),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                text = state.email
            ) {
                action(RegisterCredentialsAction.ChangeEmail(it))
            }
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                labelText = stringResource(R.string.password),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                text = state.password,
                visualTransformation = PasswordVisualTransformation()
            ) {
                action(RegisterCredentialsAction.ChangePassword(it))
            }
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                labelText = stringResource(R.string.confirm_password),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                text = state.confirmPassword,
                visualTransformation = PasswordVisualTransformation()
            ) {
                action(RegisterCredentialsAction.ChangeConfirmPassword(it))
            }
            Spacer(modifier = Modifier.height(32.dp))
            LoadingButton(
                isLoading = state.isLoading,
                buttonText = stringResource(R.string.create)
            ) {
                action(RegisterCredentialsAction.RegisterCredentials)
            }
            Spacer(modifier = Modifier.height(40.dp))
            SimplePageIndicator(selectedPage = 1, amountOfPages = 2)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
    state.error?.let {
        ErrorTreatment(state.error) {
            action(RegisterCredentialsAction.CleanErrors)
        }
    }
    if (state.showEmailSentDialog) {
        OneButtonSuccessDialog(message = stringResource(R.string.email_sent, state.email)) {
            action(RegisterCredentialsAction.ShowEmailSentDialog(false))
            openLogin()
        }
    }
}

@Composable
private fun ErrorTreatment(error: Throwable, onDismiss: () -> Unit) {
    when (error) {
        is RegisterCredentialsError.DifferentPassword -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.different_passwords),
                onDismiss = onDismiss
            )
        }
        is RegisterCredentialsError.EmptyField -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.empty_field_error),
                onDismiss = onDismiss
            )
        }
        is RegisterCredentialsError.EmailDomainNotAllowed -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.email_domain_not_allowed),
                onDismiss = onDismiss
            )
        }
        is RegisterCredentialsError.AuthWeakPassword -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.invalid_number_of_characters),
                onDismiss = onDismiss
            )
        }
        is RegisterCredentialsError.AuthUserCollision -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.user_already_exists),
                onDismiss = onDismiss
            )
        }
        is RegisterCredentialsError.AuthInvalidCredentials -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.invalid_credentials),
                onDismiss = onDismiss
            )
        }
        is RegisterCredentialsError.SendEmailVerification -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.verification_email_error),
                onDismiss = onDismiss
            )
        }
        else -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.could_not_create_account),
                onDismiss = onDismiss
            )
        }
    }
}