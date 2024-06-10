package com.practice.main

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.a11y.isLargeFont
import com.practice.designsystem.components.BlindarScrollableTabRow
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.HeadlineSmall
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.modifier.drawBottomLine
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.state.MemoDialogElement
import com.practice.main.state.Menu
import com.practice.main.state.Nutrient
import com.practice.main.state.UiMeal
import com.practice.main.state.UiMeals
import com.practice.main.state.UiMemo
import com.practice.main.state.UiSchedule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreenTopBar(
    schoolName: String,
    isLoading: Boolean,
    onRefreshIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSchoolNameClick: () -> Unit = {},
    onClickLabel: String = "",
) {
    Box(
        modifier = modifier,
    ) {
        TextButton(
            onClick = onSchoolNameClick,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable(
                    onClickLabel = onClickLabel,
                    onClick = onSchoolNameClick,
                    role = Role.Button,
                ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        ) {
            TitleLarge(
                text = schoolName,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        MainTopBarActions(
            isLoading = isLoading,
            onRefreshIconClick = onRefreshIconClick,
            onSettingsIconClick = onSettingsIconClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .wrapContentSize(),
        )
    }
}

@Composable
private fun MainTopBarActions(
    isLoading: Boolean,
    onRefreshIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontalSpace = if (LocalDensity.current.isLargeFont) 0.dp else 4.dp
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpace),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ForceRefreshIcon(
            isLoading = isLoading,
            onClick = onRefreshIconClick,
        )
        SettingsIcon(onClick = onSettingsIconClick)
    }
}

@Composable
private fun ForceRefreshIcon(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loadingTransition = rememberInfiniteTransition("loading")
    val angle by loadingTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isLoading) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = CubicBezierEasing(0.5f, 0.0f, 0.5f, 1.0f)
            ),
        ),
        label = "loading-angle",
    )
    val iconDescription = stringResource(id = R.string.main_screen_refresh_icon_description)

    IconButton(
        onClick = onClick,
        modifier = modifier
            .semantics(mergeDescendants = true) {
                contentDescription = iconDescription
            }
            .rotate(angle),
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            tint = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
        )
    }
}

@Composable
private fun SettingsIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val description = stringResource(id = R.string.main_screen_settings_icon_description)
    IconButton(
        onClick = onClick,
        modifier = modifier
            .semantics(mergeDescendants = true) {
                contentDescription = description
            },
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = null,
            tint = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
        )
    }
}

@Composable
internal fun MainScreenContents(
    uiMeals: UiMeals,
    memoDialogElements: ImmutableList<MemoDialogElement>,
    selectedMealIndex: Int,
    onMealTimeClick: (Int) -> Unit,
    onNutrientDialogOpen: () -> Unit,
    onMemoDialogOpen: () -> Unit,
    modifier: Modifier = Modifier,
    emptyContentAlignment: Alignment = Alignment.Center,
    header: @Composable (() -> Unit)? = null,
) {
    if (uiMeals.isEmpty && memoDialogElements.isEmpty()) {
        Box(modifier = modifier) {
            Column(
                modifier = Modifier
                    .align(emptyContentAlignment)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                header?.invoke()
                EmptyContentIndicator(onClick = onMemoDialogOpen)
            }
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 10.dp),
        ) {
            item {
                header?.invoke()
            }
            if (!uiMeals.isEmpty) {
                item {
                    MealContents(
                        uiMeals = uiMeals,
                        selectedIndex = selectedMealIndex,
                        onMealTimeClick = onMealTimeClick,
                        onNutrientDialogOpen = onNutrientDialogOpen,
                    )
                }
            }
            item {
                ScheduleContents(
                    scheduleElements = memoDialogElements,
                    onMemoDialogOpen = onMemoDialogOpen,
                )
            }
        }
    }
}

@Composable
private fun EmptyContentIndicator(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(8.dp),
) {
    val description = stringResource(id = R.string.main_screen_add_memo)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(padding)
            .semantics(mergeDescendants = true) {
                role = Role.Button
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            alignment = Alignment.CenterHorizontally
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
        )
        TitleMedium(text = description)
    }
}

@Composable
internal fun MealContents(
    uiMeals: UiMeals,
    selectedIndex: Int,
    onMealTimeClick: (Int) -> Unit,
    onNutrientDialogOpen: () -> Unit,
    modifier: Modifier = Modifier,
    itemPadding: Dp = 16.dp,
) {
    MainScreenContent(
        titleContent = {
            MainScreenContentHeader(
                titleContent = {
                    MealTimeButtons(
                        selectedIndex = selectedIndex,
                        mealTimes = uiMeals.mealTimes,
                        onMealTimeClick = onMealTimeClick,
                    )
                },
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(itemPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MainScreenContentItems(items = uiMeals[selectedIndex].menus) { it.name }
            MainScreenContentBottomButton(
                title = stringResource(id = R.string.open_nutrient_dialog_button),
                onButtonClick = onNutrientDialogOpen,
            )
        }
    }
}

@Composable
internal fun ScheduleContents(
    scheduleElements: ImmutableList<MemoDialogElement>,
    onMemoDialogOpen: () -> Unit,
    modifier: Modifier = Modifier,
    itemPadding: Dp = 16.dp,
) {
    if (scheduleElements.isEmpty()) {
        EmptyScheduleContent(onMemoDialogOpen = onMemoDialogOpen)
    } else {
        MainScreenContent(
            modifier = modifier,
            title = stringResource(id = R.string.schedule_content_title),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(itemPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MainScreenContentItems(items = scheduleElements) { it.displayText }
                MainScreenContentBottomButton(
                    title = stringResource(id = R.string.open_memo_dialog_button),
                    onButtonClick = onMemoDialogOpen,
                )
            }
        }
    }
}

@Composable
private fun EmptyScheduleContent(
    onMemoDialogOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MainScreenContent(
        titleContent = {},
        padding = PaddingValues(0.dp),
        contentAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        EmptyContentIndicator(
            onClick = onMemoDialogOpen,
            modifier = Modifier.fillMaxWidth(),
            padding = PaddingValues(vertical = 12.dp),
        )
    }
}

@Composable
private fun <T> MainScreenContentItems(
    items: ImmutableList<T>,
    modifier: Modifier = Modifier,
    itemToString: (T) -> String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items.forEach { item ->
            BodyLarge(text = itemToString(item))
        }
    }
}

val mainScreenDefaultPadding = PaddingValues(
    start = 20.dp,
    top = 10.dp,
    end = 20.dp,
    bottom = 20.dp,
)

@Composable
internal fun MainScreenContent(
    modifier: Modifier = Modifier,
    title: String = "",
    padding: PaddingValues = mainScreenDefaultPadding,
    contentAlignment: Alignment.Horizontal = Alignment.Start,
    contents: @Composable () -> Unit = {},
) {
    MainScreenContent(
        modifier = modifier,
        titleContent = {
            MainScreenContentHeader(title = title)
        },
        padding = padding,
        contentAlignment = contentAlignment,
        contents = contents,
    )
}

@Composable
internal fun MainScreenContent(
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit = {},
    padding: PaddingValues = mainScreenDefaultPadding,
    contentAlignment: Alignment.Horizontal = Alignment.Start,
    contents: @Composable () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = contentAlignment,
        ) {
            titleContent()
            contents()
        }
    }
}

@Composable
private fun MainScreenContentHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    MainScreenContentHeader(modifier = modifier) {
        MainScreenContentTitle(title = title)
    }
}

@Composable
private fun MainScreenContentHeader(
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        titleContent()
    }
}

@Composable
private fun MainScreenContentBottomButton(
    title: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer
    Button(
        onClick = onButtonClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        BodyLarge(
            text = title,
            color = contentColorFor(backgroundColor),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
private fun MainScreenContentTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    HeadlineSmall(
        text = title,
        modifier = modifier
            .drawBottomLine(color = MaterialTheme.colorScheme.onSurface, width = 2.dp)
            .padding(start = 60.dp, end = 60.dp, bottom = 4.dp),
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun MealTimeButtons(
    selectedIndex: Int,
    mealTimes: ImmutableList<String>,
    onMealTimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BlindarScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.drawBottomLine(color = MaterialTheme.colorScheme.onSurface),
        edgePadding = 0.dp,
        tabItemSpacing = 16.dp,
    ) {
        mealTimes.forEachIndexed { index, mealTime ->
            MealTimeButton(
                mealTime = mealTime,
                isSelected = index == selectedIndex,
                onClick = { onMealTimeClick(index) },
            )
        }
    }
}

@Composable
private fun MealTimeButton(
    mealTime: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.6f,
        label = "alpha $mealTime",
    )
    val description = stringResource(
        id = if (isSelected) R.string.meal_time_button_description_selected else R.string.meal_time_button_description,
        mealTime
    )
    val clickLabel = stringResource(id = R.string.meal_time_button_label, mealTime)

    HeadlineSmall(
        text = mealTime,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = alpha),
        modifier = modifier
            .clickable(
                onClickLabel = clickLabel,
                onClick = onClick,
            )
            .clearAndSetSemantics {
                role = Role.Button
                contentDescription = description
            }
            .padding(bottom = 4.dp),
    )
}

val previewMenus = listOf("찰보리밥", "망고마들렌", "쇠고기미역국", "콩나물파채무침", "돼지양념구이", "포기김치", "오렌지주스", "기타등등")
    .map { Menu(it) }.toImmutableList()
val previewNutrients = (0..3).map { Nutrient("탄수화물", 123.0, "g") }.toImmutableList()
val previewSchedules = (0..6).map {
    UiSchedule(
        schoolCode = 1,
        year = 2023,
        month = 7,
        day = it + 1,
        id = it.toLong(),
        eventName = "학사일정 $it",
        eventContent = "$it"
    )
}.toImmutableList()
val previewMemos = (1..3).map {
    UiMemo(
        id = it.toString(),
        userId = "blindar",
        year = 2022,
        month = 10,
        day = 11,
        contents = "memo $it",
        isSavedOnRemote = false,
    )
}.toImmutableList()

@DarkPreview
@Composable
private fun MainScreenContentHeaderButtonPreview() {
    BlindarTheme {
        MainScreenContentBottomButton(
            title = "영양 정보",
            onButtonClick = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        )
    }
}

internal val sampleUiMeals = UiMeals(
    listOf("조식", "중식", "석식").map {
        UiMeal(2022, 10, 28, it, previewMenus, previewNutrients)
    }
)

@DarkPreview
@Composable
private fun MealContentPreview() {
    var selectedMealIndex by remember { mutableIntStateOf(0) }
    BlindarTheme {
        MealContents(
            uiMeals = sampleUiMeals,
            selectedIndex = selectedMealIndex,
            onMealTimeClick = { selectedMealIndex = it },
            onNutrientDialogOpen = {},
        )
    }
}

@DarkPreview
@Composable
private fun ScheduleContentPreview() {
    BlindarTheme {
        ScheduleContents(
            scheduleElements = previewSchedules,
            onMemoDialogOpen = {},
        )
    }
}

@DarkPreview
@Composable
private fun MainScreenContentMealTimesTitlePreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    BlindarTheme {
        MealTimeButtons(
            selectedIndex = selectedIndex,
            mealTimes = persistentListOf("조식", "중식", "석식"),
            onMealTimeClick = { selectedIndex = it },
            modifier = Modifier
                .padding(16.dp),
        )
    }
}

@DarkPreview
@Composable
private fun MainScreenContentTitlePreview() {
    BlindarTheme {
        MainScreenContentTitle(
            title = "일정",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        )
    }
}