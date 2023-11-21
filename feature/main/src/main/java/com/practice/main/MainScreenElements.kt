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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.DisplayMedium
import com.practice.designsystem.components.DisplaySmall
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.popup.NutrientPopup
import com.practice.main.popup.popupPadding
import com.practice.main.state.DailyData
import com.practice.main.state.MealUiState
import com.practice.main.state.MemoPopupElement
import com.practice.main.state.MemoUiState
import com.practice.main.state.Menu
import com.practice.main.state.Nutrient
import com.practice.main.state.ScheduleUiState
import com.practice.main.state.ScreenModeIcon
import com.practice.main.state.UiMemo
import com.practice.main.state.UiSchedule
import com.practice.main.state.mergeSchedulesAndMemos
import com.practice.main.state.screenModeIcons
import com.practice.preferences.ScreenMode
import com.practice.util.date.daytype.toKor
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
    memoPopupElements: ImmutableList<MemoPopupElement>,
    mealColumns: Int,
    isNutrientPopupVisible: Boolean,
    onNutrientPopupOpen: () -> Unit,
    onNutrientPopupClose: () -> Unit,
    onMemoPopupOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (mealUiState.isEmpty && memoPopupElements.isEmpty()) {
        Box(modifier = modifier) {
            EmptyContentIndicator(
                onClick = onMemoPopupOpen,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 10.dp),
        ) {
            if (!mealUiState.isEmpty) {
                item {
                    MealContent(
                        mealUiState = mealUiState,
                        columns = mealColumns,
                        isNutrientPopupVisible = isNutrientPopupVisible,
                        onNutrientPopupOpen = onNutrientPopupOpen,
                        onNutrientPopupClose = onNutrientPopupClose,
                    )
                }
            }
            item {
                ScheduleContent(
                    scheduleElements = memoPopupElements,
                    onMemoPopupOpen = onMemoPopupOpen,
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
    val description = stringResource(id = R.string.main_screen_add_memo_description)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(padding)
            .clearAndSetSemantics {
                contentDescription = description
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
        TitleMedium(text = stringResource(id = R.string.main_screen_add_memo))
    }
}

@Composable
internal fun MealContent(
    mealUiState: MealUiState,
    columns: Int,
    isNutrientPopupVisible: Boolean,
    onNutrientPopupOpen: () -> Unit,
    onNutrientPopupClose: () -> Unit,
    modifier: Modifier = Modifier,
    itemPadding: Dp = 16.dp,
) {
    MainScreenContent(
        title = "식단",
        modifier = modifier,
        buttonTitle = stringResource(id = R.string.open_nutrient_popup_button),
        onButtonClick = onNutrientPopupOpen,
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
    if (isNutrientPopupVisible) {
        val month = mealUiState.month
        val day = mealUiState.day
        MainScreenPopup(
            onClose = onNutrientPopupClose,
        ) {
            NutrientPopup(
                popupTitle = stringResource(
                    id = R.string.nutrient_popup_title,
                    "${month}월 ${day}일"
                ),
                nutrients = mealUiState.nutrients,
                onClose = onNutrientPopupClose,
                modifier = Modifier.padding(popupPadding),
            )
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
    scheduleElements: ImmutableList<MemoPopupElement>,
    onMemoPopupOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (scheduleElements.isEmpty()) {
        EmptyScheduleContent(onMemoPopupOpen = onMemoPopupOpen)
    } else {
        MainScreenContent(
            title = stringResource(id = R.string.schedule_content_title),
            modifier = modifier,
            buttonTitle = stringResource(id = R.string.open_memo_popup_button),
            onButtonClick = onMemoPopupOpen,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                scheduleElements.forEach { uiSchedule ->
                    BodyLarge(text = uiSchedule.displayText)
                }
            }
        }
    }
}

@Composable
private fun EmptyScheduleContent(
    onMemoPopupOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MainScreenContent(
        padding = PaddingValues(0.dp),
        contentAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        EmptyContentIndicator(
            onClick = onMemoPopupOpen,
            modifier = Modifier.fillMaxWidth(),
            padding = PaddingValues(vertical = 12.dp),
        )
    }
}

@Composable
fun MainScreenPopup(
    onClose: () -> Unit,
    content: @Composable () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        content()
    }
}

@Composable
internal fun DailyMealSchedules(
    items: List<DailyData>,
    selectedDate: Date,
    isNutrientPopupVisible: Boolean,
    onNutrientPopupOpen: () -> Unit,
    onNutrientPopupClose: () -> Unit,
    onMemoPopupOpen: () -> Unit,
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
                dailyData = item,
                isNutrientPopupVisible = isNutrientPopupVisible,
                onNutrientPopupOpen = onNutrientPopupOpen,
                onNutrientPopupClose = onNutrientPopupClose,
                onMemoPopupOpen = onMemoPopupOpen,
                mealColumns = mealColumns,
                onDateClick = onDateClick,
            )
        }
    }
}

@Composable
internal fun DailyMealSchedule(
    dailyData: DailyData,
    isNutrientPopupVisible: Boolean,
    onNutrientPopupOpen: () -> Unit,
    onNutrientPopupClose: () -> Unit,
    onMemoPopupOpen: () -> Unit,
    modifier: Modifier = Modifier,
    mealColumns: Int = 2,
    onDateClick: (Date) -> Unit = {},
) {
    val date = dailyData.date
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
            mealUiState = dailyData.mealUiState,
            memoPopupElements = dailyData.memoPopupElements,
            mealColumns = mealColumns,
            isNutrientPopupVisible = isNutrientPopupVisible,
            onNutrientPopupOpen = onNutrientPopupOpen,
            onNutrientPopupClose = onNutrientPopupClose,
            onMemoPopupOpen = onMemoPopupOpen,
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
    modifier: Modifier = Modifier,
    title: String = "",
    buttonTitle: String = "",
    onButtonClick: () -> Unit = {},
    padding: PaddingValues = PaddingValues(
        start = 25.dp,
        top = 10.dp,
        end = 25.dp,
        bottom = 30.dp,
    ),
    contentAlignment: Alignment.Horizontal = Alignment.Start,
    contents: @Composable () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = contentAlignment,
        ) {
            if (title.isNotEmpty() || buttonTitle.isNotEmpty()) {
                MainScreenContentHeader(
                    title = title,
                    buttonTitle = buttonTitle,
                    onButtonClick = onButtonClick,
                )
            }
            contents()
        }
    }
}

@Composable
private fun MainScreenContentHeader(
    title: String,
    modifier: Modifier = Modifier,
    buttonTitle: String = "",
    onButtonClick: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MainScreenContentTitle(title = title)
        Spacer(modifier = Modifier.weight(1f))
        if (buttonTitle != "") {
            MainScreenContentHeaderButton(title = buttonTitle, onButtonClick = onButtonClick)
        }
    }
}

@Composable
private fun MainScreenContentHeaderButton(
    title: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.primary
    Button(
        onClick = onButtonClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
    ) {
        LabelLarge(
            text = title,
            textColor = contentColorFor(backgroundColor)
        )
    }
}

@Composable
private fun MainScreenContentTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    TitleLarge(
        text = title,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
    )
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
val previewNutrients = (0..3).map { Nutrient("탄수화물", 123.0, "g") }.toImmutableList()
val previewSchedules = (0..6).map {
    UiSchedule(
        schoolCode = 1,
        year = 2023,
        month = 7,
        day = it + 1,
        id = it,
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

@LightAndDarkPreview
@Composable
private fun MainScreenContentHeaderButtonPreview() {
    BlindarTheme {
        MainScreenContentHeaderButton(
            title = "영양 정보",
            onButtonClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

private val sampleMealUiState = MealUiState(2022, 10, 28, previewMenus, previewNutrients)

@Preview(showBackground = true)
@Composable
private fun MainScreenContentsPreview() {
    BlindarTheme {
        MainScreenContents(
            mealUiState = sampleMealUiState,
            memoPopupElements = mergeSchedulesAndMemos(
                ScheduleUiState(
                    date = Date.now(),
                    uiSchedules = previewSchedules,
                ),
                MemoUiState(
                    date = Date.now(),
                    memos = previewMemos,
                ),
            ),
            mealColumns = 2,
            isNutrientPopupVisible = false,
            onNutrientPopupOpen = {},
            onNutrientPopupClose = {},
            onMemoPopupOpen = {},
            modifier = Modifier.height(500.dp),
        )
    }
}

@LightPreview
@Composable
private fun MealContentPreview() {
    BlindarTheme {
        MealContent(
            mealUiState = sampleMealUiState,
            columns = 2,
            isNutrientPopupVisible = false,
            onNutrientPopupOpen = {},
            onNutrientPopupClose = {},
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun ListScreenItemPreview() {
    val date = Date(2022, 12, 13)
    BlindarTheme {
        DailyMealSchedule(
            DailyData(
                schoolCode = 1,
                date = date,
                mealUiState = sampleMealUiState,
                scheduleUiState = ScheduleUiState(
                    date = date,
                    uiSchedules = previewSchedules,
                ),
                memoUiState = MemoUiState(
                    date = date,
                    memos = previewMemos,
                ),
            ),
            isNutrientPopupVisible = false,
            onNutrientPopupOpen = {},
            onNutrientPopupClose = {},
            onMemoPopupOpen = {},
        )
    }
}