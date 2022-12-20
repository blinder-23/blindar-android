package com.practice.hanbitlunch.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.core.CalendarPage
import com.practice.hanbitlunch.calendar.core.CalendarState
import com.practice.hanbitlunch.calendar.core.TextsInsideDate
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import com.practice.hanbitlunch.calendar.core.yearMonth
import com.practice.hanbitlunch.theme.HanbitCalendarTheme

internal val calendarDateShape = CircleShape.copy(all = CornerSize(5.dp))
internal val largeCalendarDateShape = CircleShape.copy(all = CornerSize(15.dp))

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    calendarPage: CalendarPage = CalendarPage.getInstance(calendarState.yearMonth),
    getContentDescription: (Date) -> String = { "" },
    getClickLabel: (Date) -> String? = { null },
    onDateClick: (Date) -> Unit = {},
    dateArrangement: Arrangement.Vertical = Arrangement.Center,
    dateShape: Shape = calendarDateShape,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    dateBelowContent: @Composable (Date) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
    ) {
        CalendarDays(
            days = calendarDays(),
            modifier = Modifier
                .clearAndSetSemantics {}
        )
        CalendarDates(
            page = calendarPage,
            selectedDate = calendarState.selectedDate,
            getContentDescription = getContentDescription,
            getClickLabel = getClickLabel,
            onDateClick = {
                calendarState.selectedDate = it
                onDateClick(it)
            },
            dateShape = dateShape,
            dateArrangement = dateArrangement,
            drawBehindElement = drawBehindElement,
            dateBelowContent = dateBelowContent,
        )
    }
}

@Composable
fun LargeCalendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    calendarPage: CalendarPage = CalendarPage.getInstance(calendarState.yearMonth),
    getContentDescription: (Date) -> String = { "" },
    getClickLabel: (Date) -> String? = { null },
    onDateClick: (Date) -> Unit = {},
    dateArrangement: Arrangement.Vertical = Arrangement.spacedBy(5.dp),
    dateShape: Shape = largeCalendarDateShape,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    dateBelowContent: @Composable (Date) -> Unit = {},
) {
    Calendar(
        modifier = modifier,
        calendarState = calendarState,
        calendarPage = calendarPage,
        getContentDescription = getContentDescription,
        getClickLabel = getClickLabel,
        onDateClick = onDateClick,
        dateArrangement = dateArrangement,
        dateShape = dateShape,
        drawBehindElement = drawBehindElement,
        dateBelowContent = dateBelowContent
    )
}

@Preview(name = "Small", showBackground = true, widthDp = 400, heightDp = 300)
@Composable
private fun CalendarPreview() {
    val calendarState = rememberCalendarState(
        year = 2022,
        month = 8,
        selectedDate = Date(2022, 8, 12)
    )
    HanbitCalendarTheme(darkTheme = true) {
        Column {
            Calendar(
                calendarState = calendarState,
                dateShape = CircleShape.copy(all = CornerSize(5.dp)),
            )
        }
    }
}

@Preview(name = "Large", showBackground = true, widthDp = 800, heightDp = 1200)
@Composable
private fun LargeCalendarPreview() {
    val date = Date(2022, 8, 12)
    val calendarState = rememberCalendarState(
        year = 2022,
        month = 8,
        selectedDate = date
    )
    HanbitCalendarTheme(darkTheme = true) {
        LargeCalendar(calendarState = calendarState) {
            if (it == date) {
                TextsInsideDate(
                    texts = (1..5).map { i -> "학사일정 ${i}번" },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}