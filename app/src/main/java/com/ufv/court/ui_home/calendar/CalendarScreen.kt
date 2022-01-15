package com.ufv.court.ui_home.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CustomToolbar
import com.ufv.court.core.ui.components.HorizontalDivisor
import com.ufv.court.ui_home.calendar.components.FirstWeekOfMonthItem
import com.ufv.court.ui_home.calendar.components.LastWeekOfMonthItem
import com.ufv.court.ui_home.calendar.components.MiddleWeekOfMonthItem
import com.ufv.court.ui_home.calendar.components.weekModifier
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

@Composable
fun CalendarScreen(navigateUp: () -> Unit, openSchedule: (String) -> Unit) {
    CalendarScreen(hiltViewModel(), navigateUp, openSchedule)
}

@Composable
private fun CalendarScreen(
    viewModel: CalendarViewModel,
    navigateUp: () -> Unit,
    openSchedule: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = CalendarViewState.Empty)

    CalendarScreen(viewState, navigateUp, openSchedule) { action ->
        viewModel.submitAction(action)
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun CalendarScreen(
    state: CalendarViewState,
    navigateUp: () -> Unit,
    openSchedule: (String) -> Unit,
    action: (CalendarAction) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState.firstVisibleItemIndex) {
        action(CalendarAction.LoadEventsOfMonth(index = lazyListState.firstVisibleItemIndex))
    }
    Scaffold(topBar = { CalendarToolbar(navigateUp) }) {
        Box(modifier = Modifier.fillMaxSize()) {
            val contentPadding = PaddingValues(bottom = 36.dp)
            LazyColumn(
                state = lazyListState,
                flingBehavior = rememberSnapperFlingBehavior(
                    lazyListState = lazyListState,
                    snapOffsetForItem = SnapOffsets.Start,
                    endContentPadding = contentPadding.calculateBottomPadding(),
                    maximumFlingDistance = { // Max fling = 1x scrollable width
                        (it.endScrollOffset - it.startScrollOffset).toFloat()
                    }
                ),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                itemsIndexed(state.calendarMonths) { index, month ->
                    MonthItem(month = { month }, index = index) { day ->
                        openSchedule("$day/${month.monthNumber}/${month.year}")
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarToolbar(
    navigateUp: () -> Unit
) {
    CustomToolbar(
        toolbarText = stringResource(R.string.select_a_day),
        onLeftButtonClick = navigateUp
    )
}

@Composable
private fun MonthItem(month: () -> CalendarMonth, index: Int, onDayClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        MonthItemTitle(monthName = month().name)
        Spacer(modifier = Modifier.height(8.dp))
        CalendarHorizontalDivisor()
        FirstWeekOfMonthItem(
            modifier = Modifier.weekModifier(),
            firstWeek = { month().weeks.first() },
            index = index,
            onDayClick = onDayClick
        )
        month().weeks.subList(1, month().weeks.size - 1).forEach { week ->
            MiddleWeekOfMonthItem(
                modifier = Modifier.weekModifier(),
                week = { week },
                onDayClick = onDayClick,
                index = index
            )
        }
        CalendarHorizontalDivisor()
        LastWeekOfMonthItem(
            modifier = Modifier.weekModifier(),
            lastWeek = { month().weeks.last() },
            onDayClick = onDayClick,
            index = index
        )
        CalendarHorizontalDivisor()
    }
}

@Composable
private fun MonthItemTitle(monthName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = monthName,
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5
    )
}

@Composable
fun CalendarHorizontalDivisor() {
    HorizontalDivisor(modifier = Modifier.height(0.5.dp))
}
