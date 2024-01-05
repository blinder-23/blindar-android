package com.practice.main

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.calendar.SwipeableCalendar
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.offset
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.calendar.core.yearMonth
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.state.DailyAlarmIconState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun MainScreenTopBar(
    schoolName: String,
    isLoading: Boolean,
    onRefreshIconClick: () -> Unit,
    iconState: DailyAlarmIconState,
    onAlarmIconClick: () -> Unit,
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
            alarmIconState = iconState,
            onAlarmIconClick = onAlarmIconClick,
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
    alarmIconState: DailyAlarmIconState,
    onAlarmIconClick: () -> Unit,
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
        DailyAlarmIcon(
            iconState = alarmIconState,
            onClick = onAlarmIconClick,
        )
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
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun DailyAlarmIcon(
    iconState: DailyAlarmIconState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val permissionWarningMessage =
        stringResource(id = R.string.notification_permission_rejected_warning)
    val onPermissionGranted = {
        onClick()
        Toast.makeText(
            context,
            R.string.daily_alarm_icon_clicked_to_enabled,
            Toast.LENGTH_SHORT
        ).show()
    }
    val onPermissionDenied = {
        Toast.makeText(context, permissionWarningMessage, Toast.LENGTH_SHORT).show()
    }

    val exactAlarmPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.USE_EXACT_ALARM) { granted ->
            Log.d("MainScreenElements", "exact alarm permission? $granted")
            if (granted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    } else {
        null
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                exactAlarmPermissionState?.launchPermissionRequest() ?: onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

    IconButton(
        onClick = {
            onDailyAlarmIconClick(iconState, context, launcher, onClick)
        },
        modifier = modifier,
    ) {
        Icon(
            imageVector = iconState.icon,
            contentDescription = stringResource(id = iconState.iconDescriptionId),
            tint = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
        )
    }
}

private fun onDailyAlarmIconClick(
    iconState: DailyAlarmIconState,
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    onClick: () -> Unit,
) {
    val areNotificationsEnabled = context.isNotificationAndExactAlarmEnabled()
    val toastMessageId = when (iconState) {
        DailyAlarmIconState.Loading -> null

        DailyAlarmIconState.Disabled -> {
            if (!areNotificationsEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                null
            } else if (areNotificationsEnabled) {
                onClick()
                R.string.daily_alarm_icon_clicked_to_enabled
            } else {
                null
            }
        }

        DailyAlarmIconState.Enabled -> {
            onClick()
            R.string.daily_alarm_icon_clicked_to_disabled
        }
    }
    toastMessageId?.let {
        Toast.makeText(context, toastMessageId, Toast.LENGTH_SHORT).show()
    }
}

private fun Context.isNotificationAndExactAlarmEnabled(): Boolean {
    val isNotificationEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
    val isExactAlarmEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService<AlarmManager>()!!.canScheduleExactAlarms()
    } else {
        true
    }
    return isNotificationEnabled && isExactAlarmEnabled
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
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
) {
    val currentYearMonth = YearMonth.now()
    val middlePage = calendarPageCount / 2

    val monthDiff = calendarState.yearMonth.monthDiff(currentYearMonth)
    val initialPage = (middlePage + monthDiff).coerceIn(0, calendarPageCount - 1)
    val pagerState = rememberPagerState(initialPage = initialPage) { calendarPageCount }

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
                    customActions = customActions,
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
    var isLoading by remember { mutableStateOf(false) }
    BlindarTheme {
        MainScreenTopBar(
            schoolName = "한빛맹학교",
            isLoading = isLoading,
            onRefreshIconClick = { isLoading = !isLoading },
            iconState = DailyAlarmIconState.Enabled,
            onAlarmIconClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun DailyAlarmIconPreview() {
    BlindarTheme {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            DailyAlarmIcon(
                iconState = DailyAlarmIconState.Enabled,
                onClick = {},
                modifier = Modifier
                    .padding(16.dp),
            )
            DailyAlarmIcon(
                iconState = DailyAlarmIconState.Disabled,
                onClick = {},
                modifier = Modifier
                    .padding(16.dp),
            )
            DailyAlarmIcon(
                iconState = DailyAlarmIconState.Loading,
                onClick = {},
                modifier = Modifier
                    .padding(16.dp),
            )
        }
    }
}