package com.ufv.court.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ufv.court.R

@Composable
fun OneButtonDialog(
    title: String,
    message: String,
    confirmText: String,
    iconResourceId: Int,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.testTag("OneButtonDialog"),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Icon(
                    painter = painterResource(id = iconResourceId),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.testTag("TitleOneButtonDialog"),
                    text = title,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .testTag("MessageOneButtonDialog"),
                    text = message,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier
                        .width(152.dp)
                        .height(48.dp)
                        .testTag("ButtonOneButtonDialog"),
                    onClick = onDismiss,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        modifier = Modifier.testTag("ConfirmTextOneButtonDialog"),
                        text = confirmText
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun OneButtonErrorDialog(
    title: String = stringResource(R.string.error),
    confirmText: String = stringResource(R.string.ok),
    message: String,
    onDismiss: () -> Unit
) {
    OneButtonDialog(
        title = title,
        confirmText = confirmText,
        message = message,
        iconResourceId = R.drawable.ic_error
    ) {
        onDismiss()
    }
}

@Composable
fun OneButtonSuccessDialog(
    title: String = stringResource(R.string.success),
    confirmText: String = stringResource(R.string.ok),
    message: String,
    onDismiss: () -> Unit
) {
    OneButtonDialog(
        title = title,
        confirmText = confirmText,
        message = message,
        iconResourceId = R.drawable.ic_check
    ) {
        onDismiss()
    }
}

@Composable
fun TwoButtonsDialog(
    title: String,
    message: String,
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.testTag("TwoButtonsDialog"),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .height(3.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                )
                Spacer(modifier = Modifier.height(29.dp))
                Text(
                    modifier = Modifier.testTag("TitleTwoButtonsDialog"),
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .testTag("MessageTwoButtonsDialog"),
                    text = message,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row {
                    TextButton(
                        modifier = Modifier
                            .width(136.dp)
                            .height(48.dp)
                            .testTag("LeftButtonTwoButtonsDialog"),
                        onClick = onLeftButtonClick,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            modifier = Modifier.testTag("LeftButtonTextTwoButtonsDialog"),
                            text = leftButtonText
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        modifier = Modifier
                            .width(136.dp)
                            .height(48.dp)
                            .testTag("RightButtonTwoButtonsDialog"),
                        onClick = onRightButtonClick,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            modifier = Modifier.testTag("RightButtonTextTwoButtonsDialog"),
                            text = rightButtonText
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}