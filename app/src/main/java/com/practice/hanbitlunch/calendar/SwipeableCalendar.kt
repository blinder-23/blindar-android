package com.practice.hanbitlunch.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.core.CalendarPage
import com.practice.hanbitlunch.calendar.core.CalendarState
import com.practice.hanbitlunch.calendar.core.YearMonth
import com.practice.hanbitlunch.calendar.core.offset
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import com.practice.hanbitlunch.calendar.core.yearMonth

@Composable
fun SwipeableCalendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    onDateClick: (Date) -> Unit = {},
    onSwiped: (YearMonth) -> Unit = {},
    dateShape: Shape = CircleShape,
    getContentDescription: (Date) -> String = { "" },
    getClickLabel: (Date) -> String? = { null },
    dateArrangement: Arrangement.Vertical = Arrangement.Center,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
) {
    val currentYearMonth = YearMonth.now()
    // shows from a year ago to a year after
    val itemCount = 13
    val middlePage = itemCount / 2

    val monthDiff = calendarState.yearMonth.monthDiff(currentYearMonth)
    val initialPage = (middlePage + monthDiff).coerceIn(0, itemCount - 1)
    val pagerState = rememberPagerState(initialPage = initialPage)

    // Tracks swipe gesture
    LaunchedEffect(true) {
        snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
            val offset = pageIndex - middlePage
            val (newYear, newMonth) = currentYearMonth.offset(offset)
            onSwiped(YearMonth(newYear, newMonth))
        }
    }
    // Why snapshotFlow doesn't work with selectedDate?
    LaunchedEffect(calendarState.selectedDate) {
        val newPage = middlePage + calendarState.selectedDate.yearMonth.monthDiff(currentYearMonth)
        Log.d("SwipeableCalendar", "Scroll from ${pagerState.currentPage} to $newPage")
        pagerState.animateScrollToPage(newPage)
    }

    HorizontalPager(
        count = itemCount,
        state = pagerState,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth(),
        key = { currentYearMonth.offset(it).toString() },
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