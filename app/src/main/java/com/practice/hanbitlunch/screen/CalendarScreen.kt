package com.practice.hanbitlunch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.hanbitlunch.calendar.Calendar
import com.practice.hanbitlunch.calendar.rememberCalendarState
import com.practice.hanbitlunch.components.SubTitle
import com.practice.hanbitlunch.components.Title
import com.practice.hanbitlunch.theme.HanbitCalendarTheme

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onLaunch: suspend () -> Unit = {},
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) {
        // TODO: set top app bar color to primary
        onLaunch()
    }

    val uiState = viewModel.uiState

    val calendarState = rememberCalendarState(

    )
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
    ) {
        CalendarScreenHeader(year = 2022, month = 8)
        Calendar(calendarState = calendarState,
        )
    }
}

@Composable
private fun CalendarScreenHeader(
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

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPreview() {
    HanbitCalendarTheme {
        CalendarScreen()
    }
}