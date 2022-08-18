package com.practice.hanbitlunch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.hanbitlunch.calendar.Calendar
import com.practice.hanbitlunch.calendar.rememberCalendarState
import com.practice.hanbitlunch.components.Body
import com.practice.hanbitlunch.components.SubTitle
import com.practice.hanbitlunch.components.Title
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel,
    onLaunch: suspend () -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colors.primary
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(systemBarColor)
        onLaunch()
    }

    val uiState = viewModel.uiState.value
    val calendarState = rememberCalendarState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier.background(MaterialTheme.colors.surface)) {
        Column {
            MainScreenHeader(year = uiState.year, month = uiState.month)
            Calendar(
                calendarState = calendarState,
                onDateClick = { date ->
                    coroutineScope.launch {
                        viewModel.onDateClick(date)
                    }
                },
                onSwiped = viewModel::onSwiped,
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
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        if (!mealUiState.isEmpty) {
            MainScreenContent(title = "식단") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(mealUiState.menus) {
                        Body(text = it.name)
                    }
                }
            }
        }
        if (!scheduleUiState.isEmpty) {
            MainScreenContent(title = "학사일정") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    scheduleUiState.schedules.forEach { schedule ->
                        Body(text = schedule.scheduleName)
                    }
                }
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

@Preview(showBackground = true)
@Composable
private fun MainScreenContentsPreview() {
    val data = listOf("찰보리밥", "망고마들렌", "쇠고기미역국", "콩나물파채무침", "돼지양념구이", "포기김치")
    HanbitCalendarTheme {
        MainScreenContents(
            mealUiState = MealUiState(menus = data.map { Menu(it) }.toPersistentList()),
            scheduleUiState = ScheduleUiState(schedules = persistentListOf(Schedule("방학식"))),
            modifier = Modifier
                .padding(16.dp)
                .height(400.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenContentPreview() {
    val data = listOf("찰보리밥", "망고마들렌", "쇠고기미역국", "콩나물파채무침", "돼지양념구이", "포기김치")
    HanbitCalendarTheme {
        MainScreenContent(
            title = "식단",
            modifier = Modifier
                .padding(16.dp),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(data) {
                    Body(text = it)
                }
            }
        }
    }
}