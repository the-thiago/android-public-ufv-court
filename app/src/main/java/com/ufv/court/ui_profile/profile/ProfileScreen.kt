package com.ufv.court.ui_profile.profile

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ufv.court.R
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.app.theme.Manatee
import com.ufv.court.app.theme.ShipCove
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.HorizontalDivisor
import com.ufv.court.core.ui.components.OneButtonErrorDialog
import com.ufv.court.core.ui.components.TwoButtonsDialog

@Composable
fun ProfileScreen(
    openLogin: () -> Unit,
    openChangePassword: () -> Unit
) {
    ProfileScreen(
        viewModel = hiltViewModel(),
        openLogin = openLogin,
        openChangePassword = openChangePassword
    )
}

@Composable
private fun ProfileScreen(
    viewModel: ProfileViewModel,
    openLogin: () -> Unit,
    openChangePassword: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ProfileViewState.Empty)

    ProfileScreen(
        state = viewState,
        openLogin = openLogin,
        openChangePassword = openChangePassword
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun ProfileScreen(
    state: ProfileViewState,
    openLogin: () -> Unit,
    openChangePassword: () -> Unit,
    action: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 56.dp) // toolbar height
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileTopBar(state)
        ProfileHorizontalDivisor()
        ChangePasswordItem(onClick = openChangePassword)
        ProfileHorizontalDivisor()
        ChangePersonalDataItem {
        }
        ProfileHorizontalDivisor()
        TermsItem {
        }
        ProfileHorizontalDivisor()
        VersionItem {
        }
        ProfileHorizontalDivisor()
        LogoutItem(onClick = { action(ProfileAction.ChangeShowConfirmLogoutDialog(true)) })
        Spacer(modifier = Modifier.height(16.dp))
    }
    ConfirmLogoutDialog(
        showDialog = state.showConfirmLogoutDialog,
        action = action,
        openLogin = openLogin
    )
    ErrorsDialog(error = state.error, action = action)
}

@Composable
private fun ProfileTopBar(state: ProfileViewState) {
    Spacer(modifier = Modifier.height(32.dp))
    RoundedImage(image = state.image)
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = state.name, style = MaterialTheme.typography.h5)
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = state.email, style = MaterialTheme.typography.body1, color = ShipCove)
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun RoundedImage(image: String) {
//    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
//        LocalContext.current.contentResolver, Uri.parse(uri.path)
//    )

//

//    AndroidView(modifier = Modifier.size(60.dp),
//        factory = {
//        ImageView(it).apply {
//            this.setImageURI(uri)
//        }
//    },
//        update = {
//            it.setImageURI(uri)
//        }
//    )


    Image(
        modifier = Modifier
            .size(152.dp)
            .clip(CircleShape),
        painter = rememberImagePainter(
            data = image,
            builder = {
                placeholder(R.drawable.user_photo_placeholder)
                error(R.drawable.user_photo_placeholder)
            }
        ),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
//    Image(
//        modifier = Modifier
//            .size(152.dp)
//            .clip(CircleShape),
//        bitmap = ImageBitmap(),
//        contentScale = ContentScale.Crop,
//        contentDescription = null
//    )
}

@Composable
private fun ChangePasswordItem(onClick: () -> Unit) {
    ProfileItem(text = stringResource(R.string.change_password), onClick = onClick) {
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null, tint = Manatee)
    }
}

@Composable
private fun ChangePersonalDataItem(onClick: () -> Unit) {
    ProfileItem(text = stringResource(R.string.edit_personal_data), onClick = onClick) {
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null, tint = Manatee)
    }
}

@Composable
private fun TermsItem(onClick: () -> Unit) {
    ProfileItem(
        text = stringResource(R.string.terms_and_conditions),
        onClick = onClick
    )
}

@Composable
private fun VersionItem(onClick: () -> Unit) {
    ProfileItem(text = stringResource(R.string.version), onClick = onClick) {
        Text(
            text = "0.0.1", // todo app version
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal)
        )
    }
}

@Composable
private fun LogoutItem(onClick: () -> Unit) {
    ProfileItem(
        text = stringResource(R.string.logout),
        colorText = MaterialTheme.colors.primary,
        onClick = onClick
    )
}

@Composable
private fun ProfileHorizontalDivisor() {
    HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
}

@Composable
private fun ProfileItem(
    colorText: Color = BlackRock,
    text: String,
    onClick: () -> Unit = { },
    rightContent: @Composable RowScope.() -> Unit = { }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(64.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Normal,
                color = colorText
            )
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            rightContent()
        }
    }
}

@Composable
private fun ConfirmLogoutDialog(
    showDialog: Boolean,
    action: (ProfileAction) -> Unit,
    openLogin: () -> Unit
) {
    if (showDialog) {
        TwoButtonsDialog(
            title = stringResource(R.string.confirmation),
            message = stringResource(R.string.really_want_to_logout),
            leftButtonText = stringResource(R.string.cancel),
            rightButtonText = stringResource(R.string.confirm),
            onLeftButtonClick = { action(ProfileAction.ChangeShowConfirmLogoutDialog(false)) },
            onRightButtonClick = { action(ProfileAction.ConfirmLogout(openLogin)) },
            onDismiss = { action(ProfileAction.ChangeShowConfirmLogoutDialog(false)) }
        )
    }
}

@Composable
private fun ErrorsDialog(
    error: Throwable?,
    action: (ProfileAction) -> Unit
) {
    if (error == ProfileError.LogoutError) {
        OneButtonErrorDialog(message = stringResource(R.string.could_not_logout)) {
            action(ProfileAction.CleanErrors)
        }
    }
}
