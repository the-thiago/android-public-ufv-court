package com.ufv.court.ui_home.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle

@Composable
fun HomeScreen(
    openCalendar: () -> Unit,
) {
    HomeScreen(
        viewModel = hiltViewModel(),
        openCalendar = openCalendar
    )
}


@Composable
private fun HomeScreen(viewModel: HomeViewModel, openCalendar: () -> Unit) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = HomeViewState.Empty)

    HomeScreen(viewState, openCalendar) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun HomeScreen(
    state: HomeViewState,
    openCalendar: () -> Unit,
    action: (HomeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 56.dp) // toolbar height
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        HeaderCreateEvent {
            openCalendar()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HeaderCreateEvent(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        onClick = onClick,
        elevation = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(32.dp)
                    .padding(end = 16.dp),
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
            TextAndPlusIcon(modifier = Modifier.align(Alignment.CenterStart))
        }
    }
}

@Composable
private fun TextAndPlusIcon(modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            modifier = Modifier
                .background(color = Color.White, shape = CircleShape)
                .padding(12.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.schedule_time),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
    }
}