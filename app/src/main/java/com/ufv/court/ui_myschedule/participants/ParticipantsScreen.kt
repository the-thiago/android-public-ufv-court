package com.ufv.court.ui_myschedule.participants

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.core.core_common.util.toMaskedPhone
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CircularLoading
import com.ufv.court.core.ui.components.CustomToolbar
import com.ufv.court.core.ui.components.HorizontalDivisor
import com.ufv.court.core.ui.components.RoundedImage
import com.ufv.court.core.user_service.domain.model.UserModel

@Composable
fun ParticipantsScreen(navigateUp: () -> Unit) {
    ParticipantsScreen(viewModel = hiltViewModel(), navigateUp = navigateUp)
}

@Composable
private fun ParticipantsScreen(
    viewModel: ParticipantsViewModel,
    navigateUp: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ParticipantsViewState.Empty)

    ParticipantsScreen(
        state = viewState,
        navigateUp = navigateUp
    )
}

@Composable
private fun ParticipantsScreen(
    state: ParticipantsViewState,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomToolbar(toolbarText = stringResource(R.string.participants)) {
                navigateUp()
            }
        }
    ) {
        if (state.participants == null) {
            CircularLoading()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    val textRes by remember(state.participants) {
                        if (state.participants.size > 1) {
                            mutableStateOf(R.string.participants_plural)
                        } else {
                            mutableStateOf(R.string.participants_singular)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(id = textRes, state.participants.size.toString()),
                        style = MaterialTheme.typography.h5
                    )
                }
                items(items = state.participants) {
                    UserItem(user = it)
                }
                item {
                    HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun UserItem(user: UserModel) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val arrowIconRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { expanded = !expanded }
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedImage(image = user.image, size = 72.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.h6,
                    color = BlackRock,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                modifier = Modifier.rotate(arrowIconRotation),
                onClick = { expanded = !expanded }
            ) {
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
            }
        }
        if (expanded) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = user.email, style = MaterialTheme.typography.h6)
                if (user.phone.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = user.phone.toMaskedPhone(), style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}
