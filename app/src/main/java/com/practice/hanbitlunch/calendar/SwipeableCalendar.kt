package com.practice.hanbitlunch.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.date.DayType
import com.example.domain.date.calculateDayType
import com.example.domain.date.toKor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hsk.ktx.date.Date
import com.hsk.ktx.date.DayOfWeek
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SwipeableCalendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    onDateClick: (Date) -> Unit = {},
    onSwiped: (YearMonth) -> Unit = {},
    isLight: Boolean = MaterialTheme.colors.isLight,
    getContentDescription: (Date) -> String = { "" },
    getClickLabel: (Date) -> String? = { null },
    dateAlign: Alignment = Alignment.Center,
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
            isLight = isLight,
            calendarPage = calendarPage,
            calendarState = calendarState,
            getContentDescription = getContentDescription,
            getClickLabel = getClickLabel,
            onDateClick = onDateClick,
            dateAlign = dateAlign,
            drawBehindElement = drawBehindElement,
        )
    }
}

@Composable
private fun Calendar(
    isLight: Boolean,
    calendarPage: CalendarPage,
    calendarState: CalendarState,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String?,
    onDateClick: (Date) -> Unit,
    modifier: Modifier = Modifier,
    dateAlign: Alignment = Alignment.Center,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
) {
    Column(modifier = modifier) {
        CalendarDays(
            days = calendarDays(),
            isLight = isLight,
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
            isLight = isLight,
            dateAlign = dateAlign,
            drawBehindElement = drawBehindElement,
        )
    }
}

private fun calendarDays(): List<DayOfWeek> = DayOfWeek.values().toList()

@Composable
private fun CalendarDays(
    days: List<DayOfWeek>,
    modifier: Modifier = Modifier,
    isLight: Boolean = true,
) {
    Row(modifier = modifier) {
        days.forEach { day ->
            CalendarDay(
                day = day,
                isLight = isLight,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f)
            )
        }
    }
}

/**
 * 달력 맨 윗 줄의 요일
 */
@Composable
private fun CalendarDay(
    day: DayOfWeek,
    modifier: Modifier = Modifier,
    isLight: Boolean = true,
) {
    CalendarElement(
        text = day.toKor(),
        modifier = modifier,
        textColor = day.color(isLight),
    )
}

private val Transparent = Color(0x00000000)
private val WeekdayColorOnLight = Color(0xFF000000)
private val WeekDayColorOnDark = Color(0xFFFFFFFF)
private val SaturdayColor = Color(0xFF5151FF)
private val HolidayColor = Color(0xFFFF5F5F)

@Composable
private fun DayOfWeek.color(isLight: Boolean = true) = when (this) {
    DayOfWeek.SUNDAY -> HolidayColor
    DayOfWeek.SATURDAY -> SaturdayColor
    else -> if (isLight) WeekdayColorOnLight else WeekDayColorOnDark
}

@Composable
private fun CalendarDates(
    page: CalendarPage,
    selectedDate: Date,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String?,
    modifier: Modifier = Modifier,
    onDateClick: (Date) -> Unit = {},
    isLight: Boolean = true,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    dateAlign: Alignment = Alignment.Center,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        page.forEachWeeks { week ->
            CalendarWeek(
                modifier = Modifier.weight(1f),
                week = week,
                selectedDate = selectedDate,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
                currentMonth = page.month,
                onDateClick = onDateClick,
                isLight = isLight,
                dateAlign = dateAlign,
                drawBehindElement = drawBehindElement,
            )
        }
    }
}

@Composable
private fun CalendarWeek(
    week: Week,
    selectedDate: Date,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String?,
    currentMonth: Int,
    modifier: Modifier = Modifier,
    onDateClick: (Date) -> Unit = {},
    isLight: Boolean = true,
    dateAlign: Alignment = Alignment.Center,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        week.forEach { dayOfWeek ->
            CalendarDate(
                date = dayOfWeek,
                onClick = onDateClick,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
                currentMonth = currentMonth,
                isSelected = dayOfWeek == selectedDate,
                isLight = isLight,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                dateAlign = dateAlign,
                drawBehindElement = drawBehindElement,
            )
        }
    }
}

/**
 * 실제 날짜
 */
@Composable
private fun CalendarDate(
    date: Date,
    onClick: (Date) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String?,
    modifier: Modifier = Modifier,
    currentMonth: Int = date.month,
    isSelected: Boolean = false,
    isLight: Boolean = true,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    dateAlign: Alignment = Alignment.Center,
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.secondary else Transparent,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing,
        )
    )
    val text = if (date == Date.MAX) "" else date.dayOfMonth.toString()

    CalendarElement(
        text = text,
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = background)
            .clickable(onClickLabel = getClickLabel(date)) { onClick(date) }
            .clearAndSetSemantics {
                contentDescription = "${date.clickLabel}\n${getContentDescription(date)}"
            },
        textColor = date.color(
            isSelected = isSelected,
            isLight = isLight,
            currentMonth = currentMonth
        ),
        textStyle = MaterialTheme.typography.body2,
        align = dateAlign,
        drawBehindElement = {
            drawBehindElement(date)
        },
    )
}

@Composable
private fun CalendarElement(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    align: Alignment = Alignment.Center,
    drawBehindElement: DrawScope.() -> Unit = {},
) {
    var textSize by remember { mutableStateOf(textStyle.fontSize) }
    Box(modifier = modifier) {
        Text(
            text = text,
            modifier = Modifier
                .align(align)
                .drawBehind { drawBehindElement() },
            color = textColor,
            style = textStyle,
            fontSize = textSize,
            onTextLayout = { result ->
                if (result.hasVisualOverflow) {
                    textSize = textSize.times(0.9f)
                }
            }
        )
    }
}

private val WeekDayOverMonthColor = Color(0xFF999999)

@Composable
private fun Date.color(
    isSelected: Boolean = false,
    isLight: Boolean = false,
    currentMonth: Int = this.month
) = when (calculateDayType(currentMonth)) {
    DayType.Weekday -> if (isLight || isSelected) WeekdayColorOnLight else WeekDayColorOnDark
    DayType.WeekdayOverMonth -> WeekDayOverMonthColor
    DayType.Saturday -> SaturdayColor
    DayType.SaturdayOverMonth -> Color(0xFF9999FF)
    DayType.Holiday -> HolidayColor
    DayType.HolidayOverMonth -> Color(0xFFFF9999)
}

@Preview(showBackground = true)
@Composable
private fun CalendarDayPreview() {
    HanbitCalendarTheme(darkTheme = true) {
        CalendarDay(
            day = DayOfWeek.MONDAY,
            modifier = Modifier
                .size(50.dp)
                .background(MaterialTheme.colors.surface),
            isLight = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview() {
    HanbitCalendarTheme {
        CalendarDate(
            date = Date(2022, 8, 12),
            onClick = {},
            modifier = Modifier.size(50.dp),
            getContentDescription = { "" },
            getClickLabel = { null },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview_Selected() {
    HanbitCalendarTheme {
        CalendarDate(
            date = Date(2022, 8, 17),
            onClick = {},
            isSelected = true,
            modifier = Modifier.size(50.dp),
            getContentDescription = { "" },
            getClickLabel = { null },
        )
    }
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
            SwipeableCalendar(
                calendarState = calendarState,
            )
        }
    }
}

@Preview(name = "Large", showBackground = true, widthDp = 800, heightDp = 1200)
@Composable
private fun LargeCalendarPreview() {
    val calendarState = rememberCalendarState(
        year = 2022,
        month = 8,
        selectedDate = Date(2022, 8, 12)
    )
    HanbitCalendarTheme(darkTheme = true) {
        Column {
            SwipeableCalendar(
                calendarState = calendarState,
                dateAlign = Alignment.TopCenter,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 48, heightDp = 48)
@Composable
private fun UnderlinedCalendarDatePreview() {
    HanbitCalendarTheme {
        val lineColor = MaterialTheme.colors.onSurface
        CalendarDate(
            date = Date(2022, 11, 14),
            onClick = {},
            getContentDescription = { "" },
            getClickLabel = { null },
            drawBehindElement = {
                drawUnderline(
                    color = lineColor,
                    strokeWidth = 2f,
                )
            },
        )
    }
}