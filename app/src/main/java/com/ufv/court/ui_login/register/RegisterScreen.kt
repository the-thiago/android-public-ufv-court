package com.ufv.court.ui_login.register

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CustomTextField
import com.ufv.court.core.ui.components.CustomToolbar
import com.ufv.court.core.ui.components.LoadingButton
import com.ufv.court.core.ui.components.OneButtonErrorDialog

@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = RegisterViewState.Empty)

    RegisterScreen(viewState) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
fun RegisterScreen(
    state: RegisterViewState,
    action: (RegisterAction) -> Unit
) {
    Scaffold(topBar = {
        CustomToolbar(toolbarText = stringResource(R.string.create_account)) {
            action(RegisterAction.NavigateUp)
        }
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
                action(RegisterAction.ChangeEmail(it))
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
                action(RegisterAction.ChangePassword(it))
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
                action(RegisterAction.ChangeConfirmPassword(it))
            }
            Spacer(modifier = Modifier.height(24.dp))
            LoadingButton(
                isLoading = state.isLoading,
                buttonText = stringResource(R.string.create)
            ) {
                action(RegisterAction.Register)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    state.error?.let {
        ErrorTreatment(state.error) {
            action(RegisterAction.CleanErrors)
        }
    }
}

@Composable
fun ErrorTreatment(error: Throwable, onDismiss: () -> Unit) {
    when (error) {
        is RegisterError.DifferentPassword -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.different_passwords),
                onDismiss = onDismiss
            )
        }
        is RegisterError.EmptyField -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.empty_field_error),
                onDismiss = onDismiss
            )
        }
        is RegisterError.EmailDomainNotAllowed -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.email_domain_not_allowed),
                onDismiss = onDismiss
            )
        }
        is FirebaseAuthWeakPasswordException -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.invalid_number_of_characters),
                onDismiss = onDismiss
            )
        }
        is FirebaseAuthUserCollisionException -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.user_already_exists),
                onDismiss = onDismiss
            )
        }
        is FirebaseAuthInvalidCredentialsException -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.invalid_credentials),
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