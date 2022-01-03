package com.ufv.court.ui_login.registeruser

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
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

@Composable
fun RegisterUserScreen(
    navigateUp: () -> Unit,
    openRegisterCredentials: (String) -> Unit,
) {
    RegisterUserScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openRegisterCredentials = openRegisterCredentials
    )
}

@Composable
private fun RegisterUserScreen(
    viewModel: RegisterUserViewModel,
    navigateUp: () -> Unit,
    openRegisterCredentials: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = RegisterUserViewState.Empty)

    RegisterUserScreen(
        state = viewState,
        navigateUp = navigateUp,
        openRegisterCredentials = openRegisterCredentials
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun RegisterUserScreen(
    state: RegisterUserViewState,
    navigateUp: () -> Unit,
    openRegisterCredentials: (String) -> Unit,
    action: (RegisterUserAction) -> Unit
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
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                text = state.name,
            ) {
                action(RegisterUserAction.ChangeName(it))
            }
            Spacer(modifier = Modifier.height(32.dp))
            LoadingButton(isLoading = false, buttonText = stringResource(R.string.next)) {
                action(RegisterUserAction.ContinueClick(onSuccess = openRegisterCredentials))
            }
            Spacer(modifier = Modifier.height(40.dp))
            SimplePageIndicator(selectedPage = 0, amountOfPages = 2)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
    if (state.error != null && state.error == RegisterUserError.EmptyName) {
        OneButtonErrorDialog(message = stringResource(R.string.type_your_name)) {
            action(RegisterUserAction.CleanErrors)
        }
    }
}

@Composable
fun AddProfileImage(profileUri: Uri?, action: (RegisterUserAction) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) action(RegisterUserAction.ChangeImageUri(uri))
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
