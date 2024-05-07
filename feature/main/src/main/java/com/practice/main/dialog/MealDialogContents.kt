package com.practice.main.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.MealContent
import com.practice.main.R
import com.practice.main.sampleUiMeals
import com.practice.main.state.UiMeals

@Composable
fun MealDialogContents(
    uiMeals: UiMeals,
    selectedMealIndex: Int,
    onMealTimeClick: (Int) -> Unit,
    mealColumns: Int,
    onNutrientDialogOpen: () -> Unit,
    onMealDialogClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MealContent(
            uiMeals = uiMeals,
            selectedIndex = selectedMealIndex,
            onMealTimeClick = onMealTimeClick,
            columns = mealColumns,
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

@LightAndDarkPreview
@Composable
private fun MealDialogContentsPreview() {
    var selectedMealIndex by remember { mutableIntStateOf(0) }
    BlindarTheme {
        MealDialogContents(
            uiMeals = sampleUiMeals,
            selectedMealIndex = selectedMealIndex,
            onMealTimeClick = { selectedMealIndex = it },
            mealColumns = 2,
            onNutrientDialogOpen = {},
            onMealDialogClose = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}