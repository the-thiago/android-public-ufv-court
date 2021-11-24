package com.ufv.court.ui_login.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CustomTextField
import com.ufv.court.core.ui.components.LoadingButton

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = LoginViewState.Empty)

    LoginScreen(viewState) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
fun LoginScreen(
    state: LoginViewState,
    action: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            modifier = Modifier
                .height(184.dp)
                .width(240.dp),
            painter = painterResource(id = R.drawable.ufv_logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(48.dp))
        CustomTextField(
            modifier = Modifier.testTag("EmailTextField"),
            labelText = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            text = state.email
        ) {
            action(LoginAction.ChangeEmailText(it))
        }
        Spacer(modifier = Modifier.height(24.dp))
        PasswordTextField(
            text = state.password,
            isPasswordVisible = state.isPasswordVisible,
            onTrailingIconClick = { action(LoginAction.ChangePasswordVisibility) }
        ) {
            action(LoginAction.ChangePasswordText(it))
        }
        Spacer(modifier = Modifier.height(48.dp))
        LoadingButton(
            isLoading = state.isLoading,
            buttonText = stringResource(R.string.enter)
        ) {
            action(LoginAction.Login)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { }, shape = RoundedCornerShape(24.dp)) {
            Text(
                modifier = Modifier.testTag("ForgotPasswordTextButton"),
                text = stringResource(R.string.forgot_password),
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { }, shape = RoundedCornerShape(24.dp)) {
            Text(
                modifier = Modifier.testTag("RegisterTextButton"),
                text = stringResource(R.string.I_want_to_register),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    isPasswordVisible: Boolean,
    onTrailingIconClick: () -> Unit,
    text: String,
    onTextChange: (String) -> Unit,
) {
    CustomTextField(
        modifier = modifier.testTag("PasswordTextField"),
        labelText = stringResource(R.string.password),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            IconButton(onClick = onTrailingIconClick) {
                if (isPasswordVisible) {
                    Image(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null
                    )
                } else {
                    Image(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        text = text
    ) {
        onTextChange(it)
    }
}