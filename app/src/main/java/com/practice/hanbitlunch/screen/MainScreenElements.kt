package com.practice.hanbitlunch.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.hanbitlunch.components.Body
import com.practice.hanbitlunch.components.SubTitle
import com.practice.hanbitlunch.components.Title
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import com.practice.preferences.ScreenMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainScreenHeader(
    year: Int,
    month: Int,
    isLoading: Boolean,
    selectedScreenMode: ScreenMode,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onScreenModeIconClick: (ScreenMode) -> Unit = {},
) {
    val refreshIconAlpha by animateFloatAsState(targetValue = if (isLoading) 0.5f else 1f)
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isLoading) 180f else 0f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 750,
                easing = CubicBezierEasing(0.3f, 0f, 0.7f, 1f),
            ),
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(start = 16.dp, top = 13.dp, end = 16.dp, bottom = 13.dp)
    ) {
        VerticalYearMonth(
            year = year,
            month = month,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(bottom = 11.dp)
        )

        RefreshIcon(
            isLoading = isLoading,
            onRefresh = onRefresh,
            iconAlpha = { refreshIconAlpha },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .rotate(angle),
        )
        ScreenModeIconButtons(
            modifier = Modifier.align(Alignment.BottomEnd),
            screenModeIcons = screenModeIcons,
            selectedMode = selectedScreenMode,
            onIconClick = onScreenModeIconClick,
        )
    }
}

@Composable
fun RefreshIcon(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    iconAlpha: () -> Float,
    modifier: Modifier = Modifier,
) {
    IconButton(
        enabled = !isLoading,
        onClick = onRefresh,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Filled.Cached,
            contentDescription = "새로고침하기",
            tint = MaterialTheme.colors.onPrimary.copy(alpha = iconAlpha()),
        )
    }
}

@Composable
private fun ScreenModeIconButtons(
    screenModeIcons: List<ScreenModeIcon>,
    onIconClick: (ScreenMode) -> Unit,
    selectedMode: ScreenMode,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        screenModeIcons.forEach { (screenMode, icon) ->
            ScreenModeIconButton(
                icon = icon,
                onClick = { onIconClick(screenMode) },
                isSelected = (screenMode == selectedMode),
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
) {
    val transition = updateTransition(targetState = isSelected, label = "isSelected")
    val elevation by transition.animateDp(label = "transition") {
        if (it) 10.dp else 0.dp
    }
    val alpha by transition.animateFloat(label = "alpha") {
        if (it) 1f else 0.7f
    }
    val backgroundColor by transition.animateColor(label = "background") {
        if (it) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
    }
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(CircleShape.copy(all = CornerSize(4.dp)))
            .shadow(elevation = elevation)
            .background(backgroundColor)
            .alpha(alpha),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary,
        )
    }
}

@Composable
internal fun VerticalYearMonth(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colors.onPrimary
    Column(
        verticalArrangement = Arrangement.spacedBy(13.dp),
        modifier = modifier,
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
internal fun MainScreenContents(
    mealUiState: MealUiState,
    scheduleUiState: ScheduleUiState,
    mealColumns: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        if (!mealUiState.isEmpty) {
            item {
                MealContent(mealUiState = mealUiState, columns = mealColumns)
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
internal fun MealContent(
    mealUiState: MealUiState,
    columns: Int,
) {
    MainScreenContent(title = "식단") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
            Body(
                text = it.name,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun ScheduleContent(scheduleUiState: ScheduleUiState) {
    MainScreenContent(title = "학사일정") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            scheduleUiState.schedules.forEach { schedule ->
                Body(text = schedule.displayText)
            }
        }
    }
}

@Composable
internal fun MainScreenContent(
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
            isLoading = false,
            modifier = Modifier.fillMaxWidth(),
            selectedScreenMode = ScreenMode.Default,
        )
    }
}

@Preview
@Composable
private fun ScreenModeIconButtonPreview() {
    val (_, icon) = screenModeIcons.first()
    var isSelected by remember { mutableStateOf(false) }
    HanbitCalendarTheme {
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
    HanbitCalendarTheme {
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
    HanbitCalendarTheme {
        MainScreenContents(
            modifier = Modifier.height(320.dp),
            mealUiState = MealUiState(previewMenus),
            scheduleUiState = ScheduleUiState(previewSchedules),
            mealColumns = 2,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MealContentPreview() {
    HanbitCalendarTheme {
        MealContent(
            mealUiState = MealUiState(previewMenus),
            columns = 2,
        )
    }
}