package com.practice.designsystem.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.CalendarPage
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.calendar.core.yearMonth
import com.practice.designsystem.theme.BlindarTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

val calendarDateShape = CircleShape
val largeCalendarDateShape = calendarDateShape

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    calendarPage: CalendarPage = CalendarPage.getInstance(calendarState.yearMonth),
    getContentDescription: (Date) -> String = { "" },
    getClickLabel: (Date) -> String? = { null },
    onDateClick: (Date) -> Unit = {},
    dateShape: Shape = calendarDateShape,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
    isLarge: Boolean = false,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CalendarDays(
            days = calendarDays(),
            modifier = Modifier.clearAndSetSemantics {},
            isLarge = isLarge,
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
            drawBehindElement = drawBehindElement,
            customActions = customActions,
            isLarge = isLarge,
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
    dateShape: Shape = largeCalendarDateShape,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
) {
    Calendar(
        modifier = modifier,
        calendarState = calendarState,
        calendarPage = calendarPage,
        getContentDescription = getContentDescription,
        getClickLabel = getClickLabel,
        onDateClick = onDateClick,
        dateShape = dateShape,
        drawBehindElement = drawBehindElement,
        isLarge = true,
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
    BlindarTheme(darkTheme = true) {
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
    BlindarTheme(darkTheme = true) {
        LargeCalendar(calendarState = calendarState)
    }
}