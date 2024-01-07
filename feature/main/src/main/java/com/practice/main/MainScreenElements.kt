package com.practice.main

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.popup.NutrientPopup
import com.practice.main.popup.popupPadding
import com.practice.main.state.MemoPopupElement
import com.practice.main.state.Menu
import com.practice.main.state.Nutrient
import com.practice.main.state.UiMeal
import com.practice.main.state.UiMemo
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedule
import com.practice.main.state.UiSchedules
import com.practice.main.state.mergeSchedulesAndMemos
import kotlinx.collections.immutable.ImmutableList
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
        TitleLarge(
            text = schoolName,
            textColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable(onClickLabel = onClickLabel, onClick = onSchoolNameClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )
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
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
    uiMeal: UiMeal,
    memoPopupElements: ImmutableList<MemoPopupElement>,
    mealColumns: Int,
    isNutrientPopupVisible: Boolean,
    onNutrientPopupOpen: () -> Unit,
    onNutrientPopupClose: () -> Unit,
    onMemoPopupOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiMeal.isEmpty && memoPopupElements.isEmpty()) {
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
            if (!uiMeal.isEmpty) {
                item {
                    MealContent(
                        uiMeal = uiMeal,
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
    uiMeal: UiMeal,
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
            uiMeal.menus.chunked(columns).forEach { menus ->
                val filledMenus = fillMenus(menus, columns)
                MenuRow(menus = filledMenus)
            }
        }
    }
    if (isNutrientPopupVisible) {
        val month = uiMeal.month
        val day = uiMeal.day
        MainScreenPopup(
            onClose = onNutrientPopupClose,
        ) {
            NutrientPopup(
                popupTitle = stringResource(
                    id = R.string.nutrient_popup_title,
                    "${month}월 ${day}일"
                ),
                nutrients = uiMeal.nutrients,
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

private val sampleUiMeal = UiMeal(2022, 10, 28, previewMenus, previewNutrients)

@Preview(showBackground = true)
@Composable
private fun MainScreenContentsPreview() {
    BlindarTheme {
        MainScreenContents(
            uiMeal = sampleUiMeal,
            memoPopupElements = mergeSchedulesAndMemos(
                UiSchedules(
                    date = Date.now(),
                    uiSchedules = previewSchedules,
                ),
                UiMemos(
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

@LightAndDarkPreview
@Composable
private fun MainScreenTopBarPreview() {
    var isLoading by remember { mutableStateOf(false) }
    BlindarTheme {
        MainScreenTopBar(
            schoolName = "한빛맹학교",
            isLoading = isLoading,
            onRefreshIconClick = { isLoading = !isLoading },
            onSettingsIconClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}