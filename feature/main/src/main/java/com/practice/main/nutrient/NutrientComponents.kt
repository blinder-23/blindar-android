package com.practice.main.nutrient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.HeadlineSmall
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R

@Composable
internal fun NutrientItemLarge(
    name: String,
    amount: Double,
    unit: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(itemPadding),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            HeadlineSmall(text = name)
            Spacer(modifier = Modifier.weight(1f))
            HeadlineSmall(text = stringResource(id = R.string.nutrient_item_amount, amount, unit))
        }
    }
}

@Composable
internal fun NutrientItemMedium(
    name: String,
    amount: Double,
    unit: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(itemPadding),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            TitleLarge(text = name)
            Spacer(modifier = Modifier.weight(1f))
            TitleLarge(text = stringResource(id = R.string.nutrient_item_amount, amount, unit))
        }
    }
}

@Composable
internal fun NutrientItemSmall(
    name: String,
    amount: Double,
    unit: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(itemPadding),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            TitleMedium(text = name)
            Spacer(modifier = Modifier.weight(1f))
            TitleMedium(text = stringResource(id = R.string.nutrient_item_amount, amount, unit))
        }
    }
}

private val itemPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)

@DarkPreview
@Composable
private fun NutrientItemsPreview() {
    BlindarTheme {
        Column {
            NutrientItemLarge(
                name = "열량",
                amount = 123.456,
                unit = "kcal",
                modifier = Modifier.fillMaxWidth(),
            )
            NutrientItemMedium(
                name = "열량",
                amount = 123.456,
                unit = "kcal",
                modifier = Modifier.fillMaxWidth(),
            )
            NutrientItemSmall(
                name = "열량",
                amount = 123.456,
                unit = "kcal",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}