package com.practice.main.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.a11y.isLargeFont
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.previewMenus
import com.practice.main.state.Nutrient
import com.practice.main.state.UiMeal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NutrientPopup(
    uiMeal: UiMeal,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipTargetNames = listOf("열량", "탄수화물", "단백질", "지방")
    val (importantNutrients, otherNutrients) = uiMeal.nutrients.partition { nutrient ->
        chipTargetNames.contains(nutrient.name)
    }

    val month = uiMeal.month
    val day = uiMeal.day
    val mealTime = uiMeal.mealTime
    val popupTitle =
        stringResource(id = R.string.nutrient_popup_title, "${month}월 ${day}일 $mealTime")

    val shape = RoundedCornerShape(16.dp)
    LazyColumn(
        modifier = modifier
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .heightIn(max = 550.dp), // TODO: 높이 어떻게?
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            PopupTitleLarge(
                text = popupTitle,
                color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
            )
        }
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                ImportantNutrients(importantNutrients.toImmutableList())
                NutrientList(
                    nutrients = otherNutrients.toImmutableList(),
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
        item {
            NutrientPopupCloseButton(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ImportantNutrients(
    importantNutrients: ImmutableList<Nutrient>,
    modifier: Modifier = Modifier,
) {
    if (LocalDensity.current.isLargeFont) {
        NutrientList(
            nutrients = importantNutrients,
            modifier = modifier,
        )
    } else {
        NutrientChipGrid(
            nutrients = importantNutrients,
            modifier = modifier,
        )
    }
}

@Composable
private fun NutrientChipGrid(
    nutrients: ImmutableList<Nutrient>,
    modifier: Modifier = Modifier
) {
    val chipColors =
        listOf(Color(0xFFFFE2E5), Color(0xFFFFF4DE), Color(0xFFDCFCE7), Color(0xFFF3E8FF))
    val columns = 2
    val rows = nutrients.chunked(columns)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rows.forEachIndexed { rowIndex, row ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                row.forEachIndexed { colIndex, item ->
                    val index = rowIndex * columns + colIndex
                    NutrientChip(
                        nutrient = item,
                        backgroundColor = chipColors[index],
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun NutrientList(
    nutrients: ImmutableList<Nutrient>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        nutrients.forEach { nutrient ->
            NutrientListItem(nutrient)
        }
    }
}

@Composable
private fun NutrientListItem(
    nutrient: Nutrient,
    modifier: Modifier = Modifier,
) {
    val textColor = contentColorFor(MaterialTheme.colorScheme.surface)
    Row(modifier = modifier
        .clearAndSetSemantics {
            contentDescription = nutrient.description
        }
        .padding(16.dp)
    ) {
        PopupBodySmall(
            text = nutrient.name,
            color = textColor
        )
        Spacer(modifier = Modifier.weight(1f))
        PopupBodySmall(
            text = "${nutrient.amount} ${nutrient.unit}",
            color = textColor,
        )
    }
}

@Composable
private fun NutrientChip(
    nutrient: Nutrient,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
) {
    val textColor = Color.Black
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clearAndSetSemantics {
                contentDescription = nutrient.description
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PopupTitleLarge(
                text = "${nutrient.amount}${nutrient.unit}",
                color = textColor,
            )
            PopupBodyMedium(
                text = nutrient.name,
                color = textColor,
            )
        }
    }
}

@Composable
private fun NutrientPopupCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        PopupBodySmall(
            text = stringResource(id = R.string.nutrient_popup_close),
            modifier = Modifier.align(Alignment.Center),
            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

private val previewNutrients = persistentListOf(
    Nutrient("열량", 765.8, "kcal"),
    Nutrient("탄수화물", 110.2, "g"),
    Nutrient("단백질", 34.9, "g"),
    Nutrient("지방", 32.0, "g"),
    Nutrient("비타민A", 595.2, "R.E"),
    Nutrient("티아민", 0.5, "mg"),
    Nutrient("리보플라민", 0.8, "mg"),
    Nutrient("비타민C", 11.3, "mg"),
    Nutrient("칼슘", 322.3, "mg"),
    Nutrient("철분", 5.2, "mg"),
)

@LightAndDarkPreview
@Composable
private fun NutrientChipPreview() {
    BlindarTheme {
        NutrientChip(
            nutrient = previewNutrients[0],
            backgroundColor = Color(0xFFFFE2E5),
            modifier = Modifier.size(150.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NutrientListPreview() {
    BlindarTheme {
        NutrientList(
            nutrients = previewNutrients.subList(0, 4),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NutrientPopupCloseButtonPreview() {
    val coroutineScope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }
    BlindarTheme {
        NutrientPopupCloseButton(
            onClick = {
                coroutineScope.launch {
                    isVisible = true
                    delay(200L)
                    isVisible = false
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        )
        if (isVisible) {
            Text(text = "클릭 실행됨")
        }
    }
}

@LightAndDarkPreview
@Composable
private fun NutrientPopupPreview() {
    BlindarTheme {
        val now = Date.now()
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            NutrientPopup(
                uiMeal = UiMeal(
                    now.year,
                    now.month,
                    now.dayOfMonth,
                    "중식",
                    previewMenus,
                    previewNutrients
                ),
                onClose = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
            )
        }
    }
}