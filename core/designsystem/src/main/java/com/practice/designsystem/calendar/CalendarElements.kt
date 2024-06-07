package com.practice.designsystem.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.hsk.ktx.date.DayOfWeek
import com.practice.designsystem.calendar.core.CalendarPage
import com.practice.designsystem.calendar.core.Week
import com.practice.designsystem.calendar.core.clickLabel
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.HeadlineLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.util.date.daytype.DayType
import com.practice.util.date.daytype.calculateDayType
import com.practice.util.date.daytype.toKor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


internal fun calendarDays(): List<DayOfWeek> = DayOfWeek.entries

@Composable
internal fun CalendarDays(
    days: List<DayOfWeek>,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
) {
    Row(modifier = modifier) {
        days.forEach { day ->
            CalendarDay(
                day = day,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                isLarge = isLarge,
            )
        }
    }
}

/**
 * 달력 맨 윗 줄의 요일
 */
@Composable
private fun CalendarDay(
    day: DayOfWeek,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
) {
    CalendarElement(modifier = modifier) {
        if (isLarge) {
            HeadlineLarge(
                text = day.toKor(),
                color = day.color(),
            )
        } else {
            BodyLarge(
                text = day.toKor(),
                color = day.color(),
            )
        }
    }
}

private val Transparent = Color(0x00000000)
private val SaturdayColor = Color(0xFF5151FF)
private val HolidayColor = Color(0xFFFF5F5F)

@Composable
internal fun DayOfWeek.color() = when (this) {
    DayOfWeek.SUNDAY -> HolidayColor
    DayOfWeek.SATURDAY -> SaturdayColor
    else -> MaterialTheme.colorScheme.onSurface
}

@Composable
internal fun CalendarDates(
    page: CalendarPage,
    selectedDate: Date,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String?,
    modifier: Modifier = Modifier,
    onDateClick: (Date) -> Unit = {},
    dateShape: Shape = CircleShape,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
    isLarge: Boolean = false,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        page.forEachWeeks { week ->
            CalendarWeek(
                week = week,
                selectedDate = selectedDate,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
                currentMonth = page.month,
                modifier = Modifier.weight(1f),
                onDateClick = onDateClick,
                dateShape = dateShape,
                drawBehindElement = drawBehindElement,
                customActions = customActions,
                isLarge = isLarge,
            )
        }
    }
}

@Composable
internal fun CalendarWeek(
    week: Week,
    selectedDate: Date,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String?,
    currentMonth: Int,
    modifier: Modifier = Modifier,
    onDateClick: (Date) -> Unit = {},
    dateShape: Shape = CircleShape,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
    isLarge: Boolean = false,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        week.forEach { dayOfWeek ->
            CalendarDate(
                date = dayOfWeek,
                onClick = onDateClick,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                currentMonth = currentMonth,
                isSelected = dayOfWeek == selectedDate,
                dateShape = dateShape,
                drawBehindElement = drawBehindElement,
                customActions = customActions,
                isLarge = isLarge,
            )
        }
    }
}

/**
 * 실제 날짜
 */
@Composable
internal fun CalendarDate(
    date: Date,
    onClick: (Date) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String?,
    modifier: Modifier = Modifier,
    currentMonth: Int = date.month,
    isSelected: Boolean = false,
    dateShape: Shape = CircleShape,
    drawBehindElement: DrawScope.(Date) -> Unit = {},
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
    isLarge: Boolean = false,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Transparent,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing,
        )
    )
    val textColor = date.color(currentMonth)
    val text = if (date == Date.MAX) "" else date.dayOfMonth.toString()

    CalendarElement(
        modifier = modifier
            .clip(shape = dateShape)
            .clickable(onClickLabel = getClickLabel(date)) { onClick(date) }
            .border(width = 2.dp, color = borderColor, shape = dateShape)
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .padding(5.dp)
            .clearAndSetSemantics {
                contentDescription = "${date.clickLabel}\n${getContentDescription(date)}"
                this.customActions = customActions(date)
                role = Role.Button
            }
            .drawBehind {
                drawBehindElement(date)
            }
    ) {
        if (isLarge) {
            HeadlineLarge(
                text = text,
                color = textColor,
            )
        } else {
            BodyLarge(
                text = text,
                color = textColor,
            )
        }
    }
}

@Composable
internal fun CalendarElement(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {
        content()
    }
}

@Composable
internal fun CalendarElement(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    arrangement: Arrangement.Vertical = Arrangement.Center,
    drawBehindElement: DrawScope.() -> Unit = {},
    belowContent: @Composable () -> Unit = {}
) {
    val style = MaterialTheme.typography.bodySmall
    var textSize by remember { mutableStateOf(style.fontSize) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement,
    ) {
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .drawBehind { drawBehindElement() },
            color = textColor,
            fontSize = textSize,
            onTextLayout = { result ->
                if (result.hasVisualOverflow) {
                    textSize = textSize.times(0.9f)
                }
            }
        )
        belowContent()
    }
}

private val LightWeekdayColor = Color(0xFF999999)
private val LightSaturdayColor = Color(0xFF9999FF)
private val LightSundayColor = Color(0xFFFF9999)

@Composable
private fun Date.color(currentMonth: Int = this.month) = when (calculateDayType(currentMonth)) {
    DayType.Weekday -> MaterialTheme.colorScheme.onSurface
    DayType.WeekdayOverMonth -> LightWeekdayColor
    DayType.Saturday -> SaturdayColor
    DayType.SaturdayOverMonth -> LightSaturdayColor
    DayType.Holiday -> HolidayColor
    DayType.HolidayOverMonth -> LightSundayColor
}

@Preview(showBackground = true)
@Composable
private fun CalendarDayPreview() {
    BlindarTheme(darkTheme = true) {
        CalendarDay(
            day = DayOfWeek.MONDAY,
            modifier = Modifier
                .size(50.dp)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview() {
    BlindarTheme {
        CalendarDate(
            date = Date(2022, 8, 12),
            onClick = {},
            getContentDescription = { "" },
            getClickLabel = { null },
            modifier = Modifier
                .size(50.dp)
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview_Selected() {
    BlindarTheme {
        CalendarDate(
            date = Date(2022, 8, 17),
            onClick = {},
            getContentDescription = { "" },
            getClickLabel = { null },
            modifier = Modifier
                .size(50.dp)
                .background(MaterialTheme.colorScheme.surface),
            isSelected = true,
        )
    }
}