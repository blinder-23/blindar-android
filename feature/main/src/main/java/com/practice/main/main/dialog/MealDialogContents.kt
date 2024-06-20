package com.practice.main.main.dialog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.main.MealContents
import com.practice.main.main.sampleUiMeals
import com.practice.main.state.UiMeals
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealDialogContents(
    uiMeals: UiMeals,
    mealPagerState: PagerState,
    onMealTimeClick: (Int) -> Unit,
    onNutrientDialogOpen: () -> Unit,
    onMealDialogClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MealContents(
            uiMeals = uiMeals,
            pagerState = mealPagerState,
            onMealTimeClick = onMealTimeClick,
            onNutrientDialogOpen = onNutrientDialogOpen,
        )
        CloseMealDialogButton(
            onClose = onMealDialogClose,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
        )
    }
}

@Composable
private fun CloseMealDialogButton(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.primaryContainer
    val textColor = contentColorFor(backgroundColor = background)
    Box(
        modifier = modifier
            .clickable { onClose() }
            .background(background),
    ) {
        BodyLarge(
            text = stringResource(id = R.string.meal_dialog_close),
            color = textColor,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@LightAndDarkPreview
@Composable
private fun MealDialogContentsPreview() {
    val pagerState = rememberPagerState { sampleUiMeals.mealTimes.size }
    val scope = rememberCoroutineScope()
    BlindarTheme {
        MealDialogContents(
            uiMeals = sampleUiMeals,
            mealPagerState = pagerState,
            onMealTimeClick = { scope.launch { pagerState.animateScrollToPage(it) } },
            onNutrientDialogOpen = {},
            onMealDialogClose = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}