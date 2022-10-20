package com.practice.hanbitlunch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.hanbitlunch.calendar.CalendarState
import com.practice.hanbitlunch.calendar.SwipeableCalendar
import com.practice.hanbitlunch.calendar.YearMonth
import com.practice.hanbitlunch.calendar.rememberCalendarState
import com.practice.hanbitlunch.components.Body
import com.practice.hanbitlunch.components.SubTitle
import com.practice.hanbitlunch.components.Title
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate

@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier,
    onLaunch: suspend () -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colors.primary
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(systemBarColor)
        onLaunch()
        viewModel.onLaunch()
    }

    val uiState = viewModel.uiState.value
    val calendarState = rememberCalendarState()

    val backgroundModifier = modifier.background(MaterialTheme.colors.surface)
    if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
        HorizontalMainScreen(
            modifier = backgroundModifier,
            uiState = uiState,
            calendarState = calendarState,
            onDateClick = viewModel::onDateClick,
            onSwiped = viewModel::onSwiped,
            getContentDescription = viewModel::getContentDescription,
            getClickLabel = viewModel::getClickLabel,
        )
    } else {
        VerticalMainScreen(
            modifier = backgroundModifier,
            uiState = uiState,
            calendarState = calendarState,
            onDateClick = viewModel::onDateClick,
            onSwiped = viewModel::onSwiped,
            getContentDescription = viewModel::getContentDescription,
            getClickLabel = viewModel::getClickLabel,
        )
    }
}

@Composable
private fun HorizontalMainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    calendarState: CalendarState,
    onDateClick: (LocalDate) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (LocalDate) -> String,
    getClickLabel: (LocalDate) -> String,
) {
    Column(modifier = modifier) {
        MainScreenHeader(year = uiState.year, month = uiState.month)
        Row {
            SwipeableCalendar(
                modifier = Modifier.weight(1f),
                calendarState = calendarState,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
            )
            MainScreenContents(
                mealUiState = uiState.mealUiState,
                scheduleUiState = uiState.scheduleUiState,
                modifier = Modifier
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun VerticalMainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    calendarState: CalendarState,
    onDateClick: (LocalDate) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (LocalDate) -> String,
    getClickLabel: (LocalDate) -> String,
) {
    Column(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            MainScreenHeader(year = uiState.year, month = uiState.month)
            SwipeableCalendar(
                calendarState = calendarState,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
            )
        }
        MainScreenContents(
            mealUiState = uiState.mealUiState,
            scheduleUiState = uiState.scheduleUiState,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MainScreenHeader(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colors.onPrimary
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(start = 16.dp, top = 13.dp, end = 16.dp, bottom = 23.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        SubTitle(
            text = "${year}년",
            textColor = textColor,
        )
        Title(
            text = "${month}월",
            textColor = textColor,
        )
    }
}

@Composable
private fun MainScreenContents(
    mealUiState: MealUiState,
    scheduleUiState: ScheduleUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        if (!mealUiState.isEmpty) {
            item {
                MealContent(mealUiState)
            }
        }
        if (!scheduleUiState.isEmpty) {
            item {
                ScheduleContent(scheduleUiState)
            }
        }
    }
}

@Composable
private fun MealContent(mealUiState: MealUiState) {
    MainScreenContent(title = "식단") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            mealUiState.menus.chunked(2).forEach { menus ->
                MenuRow(menus = menus)
            }
        }
    }
}

@Composable
fun MenuRow(
    menus: List<Menu>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        menus.forEach {
            Body(
                text = it.name,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ScheduleContent(scheduleUiState: ScheduleUiState) {
    MainScreenContent(title = "학사일정") {
        // 여기를 lazycolumn으로 바꾸면 됨
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            scheduleUiState.schedules.forEach { schedule ->
                Body(text = schedule.scheduleName)
            }
        }
    }
}

@Composable
private fun MainScreenContent(
    title: String,
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SubTitle(text = title)
        contents()
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenHeaderPreview() {
    HanbitCalendarTheme {
        MainScreenHeader(
            year = 2022,
            month = 8,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


val previewMenus = listOf("찰보리밥", "망고마들렌", "쇠고기미역국", "콩나물파채무침", "돼지양념구이", "포기김치", "오렌지주스")
    .map { Menu(it) }.toImmutableList()
val previewSchedules = (0..6).map { Schedule("학사일정 $it", "$it") }
    .toImmutableList()

@Preview(showBackground = true)
@Composable
private fun MainScreenContentsPreview() {
    HanbitCalendarTheme {
        MainScreenContents(
            modifier = Modifier.height(320.dp),
            mealUiState = MealUiState(previewMenus),
            scheduleUiState = ScheduleUiState(previewSchedules)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MealContentPreview() {
    HanbitCalendarTheme {
        MealContent(mealUiState = MealUiState(previewMenus))
    }
}

@Preview(showBackground = true, widthDp = 960, heightDp = 540)
@Composable
private fun HorizontalMainScreenPreview() {
    val year = 2022
    val month = 10
    val selectedDate = LocalDate.of(2022, 10, 11)

    val uiState = MainUiState(
        year = year,
        month = month,
        selectedDate = selectedDate,
        mealUiState = MealUiState(previewMenus),
        scheduleUiState = ScheduleUiState(previewSchedules),
    )
    val calendarState = rememberCalendarState(
        year = year,
        month = month,
        selectedDate = selectedDate,
    )
    HanbitCalendarTheme {
        HorizontalMainScreen(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            uiState = uiState,
            calendarState = calendarState,
            onDateClick = {},
            onSwiped = { },
            getContentDescription = { "" },
            getClickLabel = { "" },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalMainScreenPreview() {
    HanbitCalendarTheme {
        val year = 2022
        val month = 10
        val selectedDate = LocalDate.of(2022, 10, 11)

        val uiState = MainUiState(
            year = year,
            month = month,
            selectedDate = selectedDate,
            mealUiState = MealUiState(previewMenus),
            scheduleUiState = ScheduleUiState(previewSchedules),
        )
        val calendarState = rememberCalendarState(
            year = year,
            month = month,
            selectedDate = selectedDate,
        )
        HanbitCalendarTheme {
            VerticalMainScreen(
                modifier = Modifier.background(MaterialTheme.colors.surface),
                uiState = uiState,
                calendarState = calendarState,
                onDateClick = {},
                onSwiped = {},
                getContentDescription = { "" },
                getClickLabel = { "" },
            )
        }
    }
}