package com.ufv.court.ui_myschedule.scheduledetails.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ufv.court.R
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.app.theme.ShipCove
import com.ufv.court.app.theme.Solitude
import com.ufv.court.core.comments_service.domain.model.Comment
import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import com.ufv.court.core.ui.components.CustomTextField
import com.ufv.court.core.ui.components.RoundedImage
import com.ufv.court.ui_myschedule.scheduledetails.ScheduleDetailsAction

@Composable
fun CommentsSection(
    isSendingComment: Boolean,
    showCommentSent: Boolean,
    eventComments: ScheduleComments,
    comment: String,
    action: (ScheduleDetailsAction) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        text = stringResource(R.string.comments),
        style = MaterialTheme.typography.h6.copy(color = BlackRock)
    )
    if (eventComments.comments.isEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            text = stringResource(R.string.no_comments),
            style = MaterialTheme.typography.body2,
            color = ShipCove,
            textAlign = TextAlign.Center
        )
    } else {
        eventComments.comments.forEach { comment ->
            CommentItem(comment)
        }
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Solitude,
        elevation = 0.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    text = comment,
                    hintText = stringResource(R.string.type_a_comment),
                    maxLines = 10,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                ) {
                    action(ScheduleDetailsAction.ChangeComment(it))
                }
            }
            val iconSendColor by animateColorAsState(
                targetValue = if (comment.isBlank()) {
                    Color.Unspecified
                } else {
                    MaterialTheme.colors.primary
                }
            )
            SendButton(
                comment = comment,
                iconSendColor = iconSendColor,
                isLoading = isSendingComment,
                showCommentSent = showCommentSent,
                action = action
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SendButton(
    comment: String,
    iconSendColor: Color,
    isLoading: Boolean,
    showCommentSent: Boolean,
    action: (ScheduleDetailsAction) -> Unit
) {
    Column(modifier = Modifier.padding(end = 8.dp)) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Solitude, shape = CircleShape)
                        .padding(8.dp),
                    strokeWidth = 3.dp
                )
            }
            showCommentSent -> {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Solitude, shape = CircleShape)
                        .padding(8.dp),
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
            else -> {
                val keyboardController = LocalSoftwareKeyboardController.current
                IconButton(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Solitude, shape = CircleShape),
                    enabled = comment.isNotBlank(),
                    onClick = {
                        keyboardController?.hide()
                        action(ScheduleDetailsAction.SendComment)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = iconSendColor
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        RoundedImage(
            modifier = Modifier.padding(top = 16.dp),
            image = comment.userPhoto,
            size = 32.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Solitude, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    text = comment.userName,
                    color = BlackRock,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable { /*todo*/ }
                        .padding(6.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = comment.text)
        }
    }
}
