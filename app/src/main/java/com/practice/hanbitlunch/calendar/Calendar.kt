package com.practice.hanbitlunch.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.date.DayType
import com.example.domain.date.calculateDayType
import com.example.domain.date.toKor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.rememberPagerState
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    onDateClick: (LocalDate) -> Unit = {},
) {
    val calendarDays = calendarDays()
    val calendarDates = CalendarRow.getInstance(calendarState.year, calendarState.month).dates

    // variables related to swipe
    val swipeableState = rememberSwipeableState(initialValue = 0f)


    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
            .scrollable(
                state = rememberScrollState(),
                orientation = Orientation.Vertical,
                enabled = false
            ),
    ) {
        CalendarDays(days = calendarDays)
        // TODO: how to swipe this?
        CalendarDates(
            year = calendarState.year,
            month = calendarState.month,
            selectedDate = calendarState.selectedDate,
            onDateClick = {
                calendarState.selectedDate = it
                onDateClick(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun SwipeableCalendarDates(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState()
) {
    val itemCount = Int.MAX_VALUE
    val firstItemIndex = itemCount / 2

    val pagerState = rememberPagerState(initialPage = firstItemIndex)

    var offset by remember { mutableStateOf(0) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
            // TODO: set calendar state year/month
            offset = pageIndex - firstItemIndex

        }
    }

    // TODO: FIX SWIPE BEHAVIOR
    val flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
    HorizontalPager(
        count = itemCount,
        state = pagerState,
        modifier = modifier,
        flingBehavior = object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                return with(flingBehavior) {
                    performFling(initialVelocity * 0.5f)
                }
            }
        }
    ) { index ->
        val (currentYear, currentMonth) = calendarState.offset(index - firstItemIndex)
        CalendarDates(
            year = currentYear,
            month = currentMonth,
            selectedDate = calendarState.selectedDate,
            onDateClick = {
                calendarState.selectedDate = it
            }
        )
    }
    Text(
        text = offset.toString(),
        style = MaterialTheme.typography.h2
    )
}

private fun CalendarState.offset(offset: Int): List<Int> {
    var year = this.year
    var month = this.month
    month += offset
    if (month <= 0) {
        while (month !in 1..12) {
            year--
            month += 12
        }
    } else if (month > 12) {
        while (month !in 1..12) {
            year++
            month -= 12
        }
    }
    return listOf(year, month)
}

@Composable
private fun SwipeContent(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

private fun calendarDays(): List<DayOfWeek> {
    val values = DayOfWeek.values().toList()
    Collections.rotate(values, 1)
    return values
}

@Composable
private fun CalendarDays(
    days: List<DayOfWeek>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier
    ) {
        items(items = days, key = { it.ordinal }) {
            CalendarDay(day = it)
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
) {
    CalendarElement(
        text = day.toKor(),
        modifier = modifier,
        textColor = day.color(),
    )
}

private val WeekdayColor = Color(0xFF000000)
private val SaturdayColor = Color(0xFF5151FF)
private val HolidayColor = Color(0xFFFF5F5F)

private fun DayOfWeek.color() = when (this) {
    DayOfWeek.SUNDAY -> HolidayColor
    DayOfWeek.SATURDAY -> SaturdayColor
    else -> WeekdayColor
}

@Composable
private fun CalendarDates(
    year: Int,
    month: Int,
    selectedDate: LocalDate,
    modifier: Modifier = Modifier,
    onDateClick: (LocalDate) -> Unit = {},
) {
    val dates = CalendarRow.getInstance(year, month).dates
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier,
    ) {
        items(items = dates, key = { it.hashCode() }) { date ->
            CalendarDate(
                date = date,
                onClick = { clickedDate ->
                    onDateClick(clickedDate)
                },
                currentMonth = month,
                isSelected = (date == selectedDate),
            )
        }
    }
}

/**
 * 실제 날짜
 */
@Composable
private fun CalendarDate(
    date: LocalDate,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    currentMonth: Int = date.monthValue,
    isSelected: Boolean = false,
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing,
        )
    )
    CalendarElement(
        text = date.dayOfMonth.toString(),
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = background)
            .clickable { onClick(date) },
        textColor = date.color(currentMonth),
        textStyle = MaterialTheme.typography.body2,
    )
}

@Composable
private fun CalendarElement(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    textStyle: TextStyle = MaterialTheme.typography.body1,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .then(modifier)
            .aspectRatio(1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = textColor,
            style = textStyle,
        )
    }
}

@Composable
private fun LocalDate.color(currentMonth: Int = this.monthValue) =
    when (calculateDayType(currentMonth)) {
        DayType.Weekday -> WeekdayColor
        DayType.WeekdayOverMonth -> Color(0xFF999999)
        DayType.Saturday -> SaturdayColor
        DayType.SaturdayOverMonth -> Color(0xFF9999FF)
        DayType.Holiday -> HolidayColor
        DayType.HolidayOverMonth -> Color(0xFFFF9999)
    }

@Preview(showBackground = true)
@Composable
private fun CalendarDayPreview() {
    HanbitCalendarTheme {
        CalendarDay(
            day = DayOfWeek.MONDAY,
            modifier = Modifier.size(50.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview() {
    HanbitCalendarTheme {
        CalendarDate(
            date = LocalDate.of(2022, 8, 12),
            onClick = {},
            modifier = Modifier.size(50.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview_Selected() {
    HanbitCalendarTheme {
        CalendarDate(
            date = LocalDate.of(2022, 8, 17),
            onClick = {},
            isSelected = true,
            modifier = Modifier.size(50.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    val calendarState = rememberCalendarState(
        year = 2022,
        month = 8,
        selectedDate = LocalDate.of(2022, 8, 12)
    )
    HanbitCalendarTheme {
        Calendar(calendarState = calendarState)
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeableCalendarDatesPreview() {
    HanbitCalendarTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SwipeableCalendarDates(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colors.surface),
                calendarState = rememberCalendarState()
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}