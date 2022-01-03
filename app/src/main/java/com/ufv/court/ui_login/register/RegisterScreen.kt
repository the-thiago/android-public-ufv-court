package com.ufv.court.ui_login.register

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

@Composable
fun RegisterScreen(
    navigateUp: () -> Unit,
    openLogin: () -> Unit
) {
    RegisterScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openLogin = openLogin
    )
}

@Composable
private fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateUp: () -> Unit,
    openLogin: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = RegisterViewState.Empty)

    RegisterScreen(
        state = viewState,
        navigateUp = navigateUp,
        openLogin = openLogin
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun RegisterScreen(
    state: RegisterViewState,
    navigateUp: () -> Unit,
    openLogin: () -> Unit,
    action: (RegisterAction) -> Unit
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
            AddProfileImage(profileUri = state.imageUri, action = action)
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                labelText = stringResource(R.string.name),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                text = state.name
            ) {
                action(RegisterAction.ChangeName(it))
            }
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
            Spacer(modifier = Modifier.height(32.dp))
            LoadingButton(
                isLoading = state.isLoading,
                buttonText = stringResource(R.string.create)
            ) {
                action(RegisterAction.RegisterCredentials)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    state.error?.let {
        ErrorTreatment(state.error) {
            action(RegisterAction.CleanErrors)
        }
    }
    if (state.showEmailSentDialog) {
        OneButtonSuccessDialog(message = stringResource(R.string.email_sent, state.email)) {
            action(RegisterAction.ShowEmailSentDialog(false))
            openLogin()
        }
    }
}

@Composable
fun AddProfileImage(profileUri: Uri?, action: (RegisterAction) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) action(RegisterAction.ChangeImageUri(uri))
        }
    )
    Box {
        TextFieldLabel(text = stringResource(R.string.photo))
        if (profileUri != null) {
            PreviewImage(uri = profileUri, onClick = { launcher.launch("image/*") })
        } else {
            RoundedPlaceHolder(onClick = { launcher.launch("image/*") })
        }
    }
}

@Composable
private fun BoxScope.PreviewImage(uri: Uri, onClick: () -> Unit) {
    val context = LocalContext.current
    val bitmap by remember(uri) {
        if (Build.VERSION.SDK_INT < 28) {
            mutableStateOf(
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            )
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            mutableStateOf(ImageDecoder.decodeBitmap(source))
        }
    }
    Image(
        modifier = Modifier
            .roundedUserPhoto(onClick)
            .align(Alignment.Center),
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.colorMatrix(colorMatrix = ColorMatrix().apply {
            setToSaturation(.25F)
        })
    )
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = stringResource(id = R.string.change),
        color = Color.White,
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun BoxScope.RoundedPlaceHolder(onClick: () -> Unit) {
    Image(
        modifier = Modifier
            .roundedUserPhoto(onClick)
            .align(Alignment.Center),
        painter = painterResource(id = R.drawable.user_photo_placeholder),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

private fun Modifier.roundedUserPhoto(onClick: () -> Unit): Modifier = this
    .size(152.dp)
    .clip(CircleShape)
    .clickable { onClick() }

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