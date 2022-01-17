package com.ufv.court.ui_myschedule.myschedule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle

@Composable
fun MyScheduleScreen() {
    MyScheduleScreen(hiltViewModel())
}

@Composable
private fun MyScheduleScreen(viewModel: MyScheduleViewModel) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = MyScheduleViewState.Empty)

    MyScheduleScreen(state = viewState) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun MyScheduleScreen(
    state: MyScheduleViewState,
    action: (MyScheduleAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 56.dp) // toolbar height
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ExpandableSection(textRes = R.string.time_as_participant) {
            Text(text = "aa")
            Text(text = "aa")
            Text(text = "aa")
        }
        Spacer(modifier = Modifier.height(16.dp))
        ExpandableSection(textRes = R.string.times_scheduled) {
            Text(text = "bb")
            Text(text = "bb")
            Text(text = "bb")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ExpandableSection(textRes: Int, content: @Composable ColumnScope.() -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    val arrowIconRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    Column {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                text = stringResource(id = textRes),
                style = MaterialTheme.typography.h5
            )
            IconButton(
                modifier = Modifier.rotate(arrowIconRotation),
                onClick = { expanded = !expanded }
            ) {
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
            }
        }
        if (expanded) {
            content()
        }
    }
}
