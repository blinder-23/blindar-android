package com.practice.main

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.calendar.SwipeableCalendar
import com.practice.designsystem.calendar.core.*
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.theme.BlindarTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreenTopBar(
    schoolName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onClickLabel: String = "",
) {
    Box(
        modifier = modifier
            .clickable(onClickLabel = onClickLabel, onClick = onClick)
    ) {
        TitleLarge(
            text = schoolName,
            textColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarCard(
    calendarPageCount: Int,
    calendarState: CalendarState,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    dateShape: Shape,
    getClickLabel: (Date) -> String,
    drawBehindElement: DrawScope.(Date) -> Unit,
    modifier: Modifier = Modifier,
    dateArrangement: Arrangement.Vertical = Arrangement.Center,
) {
    val currentYearMonth = YearMonth.now()
    val middlePage = calendarPageCount / 2

    val monthDiff = calendarState.yearMonth.monthDiff(currentYearMonth)
    val initialPage = (middlePage + monthDiff).coerceIn(0, calendarPageCount - 1)
    val pagerState = rememberPagerState(initialPage = initialPage)

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
                    pagerState = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                )
                SwipeableCalendar(
                    itemCount = calendarPageCount,
                    calendarState = calendarState,
                    pagerState = pagerState,
                    onDateClick = onDateClick,
                    onPageChange = { pageIndex ->
                        val offset = pageIndex - middlePage
                        val (newYear, newMonth) = currentYearMonth.offset(offset)
                        onSwiped(YearMonth(newYear, newMonth))
                    },
                    onSelectedDateChange = {
                        val newPage =
                            middlePage + calendarState.selectedDate.yearMonth.monthDiff(
                                currentYearMonth
                            )
                        Log.d("CalendarCard", "Scroll from ${pagerState.currentPage} to $newPage")
                        pagerState.animateScrollToPage(newPage)
                    },
                    getContentDescription = getContentDescription,
                    dateShape = dateShape,
                    getClickLabel = getClickLabel,
                    dateArrangement = dateArrangement,
                    drawBehindElement = drawBehindElement,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarCardHeader(
    calendarState: CalendarState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    val scope = rememberCoroutineScope()
    val iconTint = contentColorFor(backgroundColor = backgroundColor)

    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        LabelLarge(
            text = calendarState.yearMonth.toString(),
            modifier = Modifier.align(Alignment.CenterStart),
        )
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
            iconTint = iconTint,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
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
                imageVector = Icons.Filled.ArrowForwardIos,
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

@LightAndDarkPreview
@Composable
private fun MainScreenTopBarPreview() {
    BlindarTheme {
        MainScreenTopBar(
            schoolName = "한빛맹학교",
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}