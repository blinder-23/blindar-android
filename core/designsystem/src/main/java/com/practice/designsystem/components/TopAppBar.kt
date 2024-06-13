package com.practice.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.R
import com.practice.designsystem.theme.BlindarTheme

/**
 * Top App Bar for Blindar.
 * Aligns [title] at the center of the app bar.
 * [navigationIcon] and [actions] are located at the start side and the end side of the [title], respectively.
 *
 * @param title The title of the app bar.
 * @param modifier The modifier to be applied to the app bar.
 * @param navigationIcon The navigation icon to be displayed at the start of the app bar.
 * @param actions The actions to be displayed at the end of the app bar.
 * @param bottomRadius The radius of bottom corners of the app bar.
 */
@Composable
fun BlindarTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    bottomRadius: Dp = 16.dp,
) {
    BlindarTopAppBar(
        modifier = modifier,
        title = {
            TitleLarge(text = title)
        },
        navigationIcon = navigationIcon,
        actions = actions,
        bottomRadius = bottomRadius,
    )
}

/**
 * Top App Bar for Blindar.
 * Aligns [title] at the center of the app bar.
 * [navigationIcon] and [actions] are located at the start side and the end side of the [title], respectively.
 *
 * @param title The title of the app bar.
 * @param modifier The modifier to be applied to the app bar.
 * @param navigationIcon The navigation icon to be displayed at the start of the app bar.
 * @param actions The actions to be displayed at the end of the app bar.
 * @param bottomRadius The radius of bottom corners of the app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlindarTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    bottomRadius: Dp = 16.dp,
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = bottomRadius, bottomEnd = bottomRadius)),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

/**
 * Large Top App Bar for Blindar. Inspired by material3 [LargeTopAppBar].
 *
 * [navigationIcon] and [actions] are aligned at the start and the end of the first row of the app bar, respectively.
 *
 * [title] is aligned at the start side of the second row.
 *
 * @param title The title of the app bar.
 * @param modifier The modifier to be applied to the app bar.
 * @param navigationIcon The navigation icon to be displayed at the first row of the app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlindarLargeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    LargeTopAppBar(
        title = {
            Column {
                DisplayMedium(text = title)
            }
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

object BlindarTopAppBarDefaults {
    @Composable
    fun NavigationIcon(onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = stringResource(id = R.string.back_arrow),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@DarkPreview
@Composable
private fun TopAppBarPreview() {
    BlindarTheme {
        BlindarTopAppBar(
            title = "휴대폰 인증",
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                BlindarTopAppBarDefaults.NavigationIcon { }
            },
        )
    }
}

@DarkPreview
@Composable
private fun LargeTopAppBarPreview() {
    BlindarTheme {
        BlindarLargeTopAppBar(
            title = "휴대폰 인증",
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                BlindarTopAppBarDefaults.NavigationIcon { }
            },
        )
    }
}