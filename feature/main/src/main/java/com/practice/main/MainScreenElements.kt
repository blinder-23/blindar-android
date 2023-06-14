package com.practice.main

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.date.toKor
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.DisplayMedium
import com.practice.designsystem.components.DisplaySmall
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.state.DailyMealScheduleState
import com.practice.main.state.MealUiState
import com.practice.main.state.Menu
import com.practice.main.state.Schedule
import com.practice.main.state.ScheduleUiState
import com.practice.main.state.ScreenModeIcon
import com.practice.main.state.screenModeIcons
import com.practice.preferences.ScreenMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainScreenHeader(
    year: Int,
    month: Int,
    selectedScreenMode: ScreenMode,
    modifier: Modifier = Modifier,
    screenModeIconsEnabled: Boolean = true,
    onScreenModeIconClick: (ScreenMode) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 16.dp, top = 13.dp, end = 16.dp, bottom = 13.dp)
    ) {
        VerticalYearMonth(
            year = year,
            month = month,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .semantics(mergeDescendants = true) {
                    contentDescription = "${year}년 ${month}월"
                },
        )
        ScreenModeIconButtons(
            screenModeIcons = screenModeIcons,
            selectedMode = selectedScreenMode,
            onIconClick = onScreenModeIconClick,
            enabled = screenModeIconsEnabled,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clearAndSetSemantics { },
        )
    }
}

@Composable
fun RefreshIcon(
    iconAlpha: () -> Float,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.Filled.Cached,
        contentDescription = "새로고침하기",
        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = iconAlpha()),
        modifier = modifier,
    )
}

@Composable
private fun ScreenModeIconButtons(
    screenModeIcons: List<ScreenModeIcon>,
    onIconClick: (ScreenMode) -> Unit,
    selectedMode: ScreenMode,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(modifier = modifier) {
        screenModeIcons.forEach { (screenMode, icon) ->
            ScreenModeIconButton(
                icon = icon,
                onClick = { onIconClick(screenMode) },
                isSelected = (screenMode == selectedMode),
                enabled = enabled,
            )
        }
    }
}

@Composable
private fun ScreenModeIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val transition = updateTransition(targetState = isSelected, label = "isSelected")
    val elevation by transition.animateDp(label = "transition") {
        if (it && enabled) 10.dp else 0.dp
    }
    val alpha by transition.animateFloat(label = "alpha") {
        if (it && enabled) 1f else if (enabled) 0.7f else 0f
    }
    val backgroundColor by transition.animateColor(label = "background") {
        if (it && enabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    }
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(CircleShape.copy(all = CornerSize(4.dp)))
            .background(backgroundColor)
            .shadow(elevation = elevation)
            .alpha(alpha),
        enabled = enabled,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
internal fun VerticalYearMonth(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onPrimary
    Column(
        verticalArrangement = Arrangement.spacedBy(13.dp),
        modifier = modifier,
    ) {
        DisplaySmall(
            text = "${year}년",
            textColor = textColor,
        )
        DisplayMedium(
            text = "${month}월",
            textColor = textColor,
        )
    }
}

@Composable
internal fun MainScreenContents(
    mealUiState: MealUiState,
    scheduleUiState: ScheduleUiState,
    mealColumns: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (!mealUiState.isEmpty) {
            MealContent(mealUiState = mealUiState, columns = mealColumns)
        }
        if (!scheduleUiState.isEmpty) {
            ScheduleContent(scheduleUiState)
        }
    }
}

@Composable
internal fun MealContent(
    mealUiState: MealUiState,
    columns: Int,
    modifier: Modifier = Modifier,
    itemPadding: Dp = 16.dp,
) {
    MainScreenContent(
        title = "식단",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(itemPadding)
        ) {
            mealUiState.menus.chunked(columns).forEach { menus ->
                val filledMenus = fillMenus(menus, columns)
                MenuRow(menus = filledMenus)
            }
        }
    }
}

private fun fillMenus(menus: List<Menu>, targetCount: Int): ImmutableList<Menu> {
    return if (menus.size == targetCount) {
        menus
    } else {
        val mutableMenus = menus.toMutableList()
        repeat(targetCount - menus.size) {
            mutableMenus.add(Menu(""))
        }
        mutableMenus
    }.toImmutableList()
}

@Composable
internal fun MenuRow(
    menus: ImmutableList<Menu>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
        menus.forEach {
            BodyLarge(
                text = it.name,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun ScheduleContent(
    scheduleUiState: ScheduleUiState,
    modifier: Modifier = Modifier,
) {
    MainScreenContent(
        title = "학사일정",
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            scheduleUiState.schedules.forEach { schedule ->
                BodyLarge(text = schedule.displayText)
            }
        }
    }
}

@Composable
internal fun DailyMealSchedules(
    items: List<DailyMealScheduleState>,
    selectedDate: Date,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    mealColumns: Int = 2,
    onDateClick: (Date) -> Unit = {},
) {
    LaunchedEffect(items, selectedDate) {
        val index = items.indexOfFirst { it.date == selectedDate }
        if (index != -1) {
            lazyListState.animateScrollToItem(index)
        }
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        items(
            items = items,
            key = { item -> item.date.toEpochSecond() },
        ) { item ->
            DailyMealSchedule(
                dailyMealScheduleState = item,
                mealColumns = mealColumns,
                onDateClick = onDateClick,
            )
        }
    }
}

@Composable
internal fun DailyMealSchedule(
    dailyMealScheduleState: DailyMealScheduleState,
    modifier: Modifier = Modifier,
    mealColumns: Int = 2,
    onDateClick: (Date) -> Unit = {},
) {
    val date = dailyMealScheduleState.date
    Column(modifier = modifier.fillMaxWidth()) {
        DailyDay(
            date = date,
            modifier = Modifier
                .clickable(onClick = { onDateClick(date) })
                .padding(16.dp)
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
        MainScreenContents(
            mealUiState = dailyMealScheduleState.mealUiState,
            scheduleUiState = dailyMealScheduleState.scheduleUiState,
            mealColumns = mealColumns,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun DailyDay(
    date: Date,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    Row(
        modifier = Modifier
            .background(backgroundColor)
            .then(modifier),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val textColor = contentColorFor(backgroundColor)
        DisplayMedium(
            text = date.dayOfMonth.toString().padStart(2, padChar = '0'),
            textColor = textColor,
            modifier = Modifier.alignByBaseline(),
        )
        BodyLarge(
            text = date.dayOfWeek.toKor(),
            textColor = textColor,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@Composable
internal fun MainScreenContent(
    title: String,
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 30.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DisplaySmall(text = title)
            contents()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenHeaderPreview() {
    BlindarTheme {
        MainScreenHeader(
            year = 2022,
            month = 8,
            selectedScreenMode = ScreenMode.Default,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenHeaderPreview_screenIconsDisabled() {
    BlindarTheme {
        MainScreenHeader(
            year = 2022,
            month = 8,
            selectedScreenMode = ScreenMode.Default,
            modifier = Modifier.fillMaxWidth(),
            screenModeIconsEnabled = false,
        )
    }
}

@Preview
@Composable
private fun ScreenModeIconButtonPreview() {
    val (_, icon) = screenModeIcons.first()
    var isSelected by remember { mutableStateOf(false) }
    BlindarTheme {
        ScreenModeIconButton(
            icon = icon,
            onClick = { isSelected = !isSelected },
            isSelected = isSelected,
        )
    }
}

@Preview
@Composable
private fun ScreenModeIconButtonsPreview() {
    var selectedMode by remember { mutableStateOf(ScreenMode.Default) }
    BlindarTheme {
        ScreenModeIconButtons(
            screenModeIcons = screenModeIcons,
            onIconClick = { selectedMode = it },
            selectedMode = selectedMode
        )
    }
}

val previewMenus = listOf("찰보리밥", "망고마들렌", "쇠고기미역국", "콩나물파채무침", "돼지양념구이", "포기김치", "오렌지주스", "기타등등")
    .map { Menu(it) }.toImmutableList()
val previewSchedules = (0..6).map {
    Schedule(
        scheduleName = "학사일정 $it",
        scheduleContent = "$it"
    )
}.toImmutableList()

@Preview(showBackground = true)
@Composable
private fun MainScreenContentsPreview() {
    BlindarTheme {
        MainScreenContents(
            modifier = Modifier.height(500.dp),
            mealUiState = MealUiState(previewMenus),
            scheduleUiState = ScheduleUiState(previewSchedules),
            mealColumns = 2,
        )
    }
}

@LightPreview
@Composable
private fun MealContentPreview() {
    BlindarTheme {
        MealContent(
            mealUiState = MealUiState(previewMenus),
            columns = 2,
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun ListScreenItemPreview() {
    BlindarTheme {
        DailyMealSchedule(
            DailyMealScheduleState(
                date = Date(2022, 12, 13),
                mealUiState = MealUiState(previewMenus),
                scheduleUiState = ScheduleUiState(previewSchedules),
            ),
        )
    }
}