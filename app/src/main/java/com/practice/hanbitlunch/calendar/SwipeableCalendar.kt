package com.practice.hanbitlunch.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.core.CalendarPage
import com.practice.hanbitlunch.calendar.core.CalendarState
import com.practice.hanbitlunch.calendar.core.YearMonth
import com.practice.hanbitlunch.calendar.core.offset
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import java.util.*

@OptIn(ExperimentalPagerApi::class)
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
    // shows from a year ago to a year after
    val itemCount = 13
    val firstItemIndex = itemCount / 2
    val pagerState = rememberPagerState(initialPage = firstItemIndex)

    val currentYearMonth = Date.now().let {
        YearMonth(it.year, it.month)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
            val offset = pageIndex - firstItemIndex
            val (newYear, newMonth) = currentYearMonth.offset(offset)
            calendarState.apply {
                year = newYear
                month = newMonth
            }
            onSwiped(YearMonth(newYear, newMonth))
        }
    }

    HorizontalPager(
        count = itemCount,
        state = pagerState,
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth(),
    ) { index ->
        val shownYearMonth = currentYearMonth.offset(index - firstItemIndex)
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