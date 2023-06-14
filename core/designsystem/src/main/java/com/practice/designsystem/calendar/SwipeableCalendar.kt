package com.practice.designsystem.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.CalendarPage
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.offset
import com.practice.designsystem.calendar.core.rememberCalendarState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableCalendar(
    itemCount: Int,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    calendarState: CalendarState = rememberCalendarState(),
    onDateClick: (Date) -> Unit = {},
    onPageChange: (Int) -> Unit = {},
    onSelectedDateChange: suspend () -> Unit = {},
    dateShape: Shape = CircleShape,
    getContentDescription: (Date) -> String = { "" },
    getClickLabel: (Date) -> String? = { null },
    dateArrangement: Arrangement.Vertical = Arrangement.Center,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
) {
    val currentYearMonth = YearMonth.now()
    val middlePage = itemCount / 2

    // Tracks swipe gesture
    LaunchedEffect(true) {
        snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
            onPageChange(pageIndex)
        }
    }
    // Why snapshotFlow doesn't work with selectedDate?
    LaunchedEffect(calendarState.selectedDate) {
        onSelectedDateChange()
    }

    HorizontalPager(
        pageCount = itemCount,
        state = pagerState,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth(),
        key = { it },
    ) { index ->
        val shownYearMonth = currentYearMonth.offset(index - middlePage)
        val calendarPage = CalendarPage.getInstance(shownYearMonth)
        Calendar(
            calendarPage = calendarPage,
            calendarState = calendarState,
            getContentDescription = getContentDescription,
            getClickLabel = getClickLabel,
            onDateClick = onDateClick,
            dateArrangement = dateArrangement,
            dateShape = dateShape,
            drawBehindElement = drawBehindElement,
        )
    }
}