package com.practice.main.main.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.calendar.SwipeableCalendar
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.offset
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.calendar.core.yearMonth
import com.practice.designsystem.components.DisplayMedium
import com.practice.designsystem.components.HeadlineLarge
import com.practice.designsystem.components.HeadlineSmall
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarCard(
    calendarPageCount: Int,
    calendarState: CalendarState,
    onCalendarHeaderClick: () -> Unit,
    calendarHeaderClickLabel: String,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    dateShape: Shape,
    getClickLabel: (Date) -> String,
    drawBehindElement: DrawScope.(Date) -> Unit,
    modifier: Modifier = Modifier,
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
    isLarge: Boolean = false,
) {
    val currentYearMonth = YearMonth.now()
    val middlePage = calendarPageCount / 2

    val monthDiff = calendarState.yearMonth.monthDiff(currentYearMonth)
    val initialPage = (middlePage + monthDiff).coerceIn(0, calendarPageCount - 1)
    val pagerState = rememberPagerState(initialPage = initialPage) { calendarPageCount }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(0.9f)
                    .align(Alignment.Center)
            ) {
                CalendarCardHeader(
                    calendarState = calendarState,
                    onCalendarHeaderClick = onCalendarHeaderClick,
                    calendarHeaderClickLabel = calendarHeaderClickLabel,
                    pagerState = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    isLarge = isLarge,
                )
                SwipeableCalendar(
                    itemCount = calendarPageCount,
                    pagerState = pagerState,
                    calendarState = calendarState,
                    onDateClick = onDateClick,
                    onPageChange = { pageIndex ->
                        val offset = pageIndex - middlePage
                        val (newYear, newMonth) = currentYearMonth.offset(offset)
                        onSwiped(YearMonth(newYear, newMonth))
                    },
                    onSelectedDateChange = {
                        val newPage = middlePage + calendarState.selectedDate.yearMonth
                            .monthDiff(currentYearMonth)
                        pagerState.animateScrollToPage(newPage)
                    },
                    dateShape = dateShape,
                    getContentDescription = getContentDescription,
                    getClickLabel = getClickLabel,
                    drawBehindElement = drawBehindElement,
                    customActions = customActions,
                    isLarge = isLarge,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarCardHeader(
    calendarState: CalendarState,
    onCalendarHeaderClick: () -> Unit,
    calendarHeaderClickLabel: String,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    isLarge: Boolean = false,
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable(
                    role = Role.Button,
                    onClick = onCalendarHeaderClick,
                    onClickLabel = calendarHeaderClickLabel,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLarge) {
                CalendarCardHeaderTitleLarge(
                    year = calendarState.yearMonth.year.toString(),
                    month = calendarState.yearMonth.month.toString(),
                )
            } else {
                CalendarCardHeaderTitle(
                    year = calendarState.yearMonth.year.toString(),
                    month = calendarState.yearMonth.month.toString(),
                )
            }
        }
        CalendarArrow(
            onLeftClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            onRightClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            iconTint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun CalendarCardHeaderTitle(
    year: String,
    month: String,
) {
    LabelLarge(
        text = year,
        color = MaterialTheme.colorScheme.onSurface,
    )
    HeadlineSmall(
        text = month,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
private fun CalendarCardHeaderTitleLarge(
    year: String,
    month: String,
) {
    HeadlineLarge(
        text = year,
        color = MaterialTheme.colorScheme.onSurface,
    )
    DisplayMedium(
        text = month,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
private fun CalendarArrow(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
) {
    val backButtonDescription = stringResource(id = R.string.calendar_back_arrow)
    val forwardButtonDescription = stringResource(id = R.string.calendar_next_arrow)
    Row(modifier = modifier) {
        IconButton(
            onClick = onLeftClick,
            modifier = Modifier.semantics {
                role = Role.Button
                contentDescription = backButtonDescription
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.clearAndSetSemantics { },
            )
        }
        IconButton(
            onClick = onRightClick,
            modifier = Modifier.semantics {
                role = Role.Button
                contentDescription = forwardButtonDescription
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.clearAndSetSemantics { },
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun CalendarCardPreview() {
    val calendarState = rememberCalendarState()
    BlindarTheme {
        CalendarCard(
            calendarPageCount = 13,
            calendarState = calendarState,
            onCalendarHeaderClick = {},
            calendarHeaderClickLabel = "",
            onDateClick = {},
            onSwiped = {},
            getContentDescription = { "" },
            dateShape = CircleShape,
            getClickLabel = { "" },
            drawBehindElement = {},
            modifier = Modifier
                .aspectRatio(1f)
                .wrapContentSize()
                .padding(16.dp),
        )
    }
}