package com.practice.main.nutrient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hsk.ktx.date.Date
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BlindarTopAppBarDefaults
import com.practice.designsystem.components.HeadlineMedium
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.state.UiNutrient

@Composable
fun NutrientScreen(
    onNavigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NutrientViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NutrientScreen(
        uiState = uiState,
        onNavigateToBack = onNavigateToBack,
        modifier = modifier,
    )
}

@Composable
private fun NutrientScreen(
    uiState: NutrientUiState,
    onNavigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            BlindarTopAppBar(
                title = stringResource(id = R.string.nutrient_header),
                navigationIcon = {
                    BlindarTopAppBarDefaults.NavigationIcon(onNavigateToBack)
                },
            )
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
        ) {
            NutrientScreenContents(
                uiState = uiState,
                modifier = Modifier.widthIn(500.dp),
            )
        }
    }
}

@Composable
private fun NutrientScreenContents(
    uiState: NutrientUiState,
    modifier: Modifier = Modifier,
) {
    val date = uiState.date

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        HeadlineMedium(
            text = stringResource(
                id = R.string.nutrient_date_format,
                date.month, date.dayOfMonth, date.dayOfWeek.shortName, uiState.mealTime,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is NutrientUiState.Loading -> {
                NutrientScreenLoading(modifier = Modifier.fillMaxSize())
            }

            is NutrientUiState.Success -> {
                NutrientScreenSuccess(
                    uiState = uiState,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is NutrientUiState.Fail -> {
                NutrientScreenFail(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun NutrientScreenLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun NutrientScreenSuccess(
    uiState: NutrientUiState.Success,
    modifier: Modifier = Modifier,
) {
    val (calorie, major, minor) = sortAndSplitNutrients(uiState.nutrients)
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        calorie.forEach {
            NutrientItemLarge(
                name = it.name,
                amount = it.amount,
                unit = it.unit,
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(12.dp))
        major.forEach {
            NutrientItemMedium(
                name = it.name,
                amount = it.amount,
                unit = it.unit,
            )
        }
        minor.forEach {
            NutrientItemSmall(
                name = it.name,
                amount = it.amount,
                unit = it.unit,
            )
        }
    }
}

private fun sortAndSplitNutrients(nutrients: List<UiNutrient>): Triple<List<UiNutrient>, List<UiNutrient>, List<UiNutrient>> {
    val majorNutrientsName = setOf("탄수화물", "단백질", "지방")
    val (calorie, notCalorie) = nutrients.partition { it.name == "열량" }
    val (major, minor) = notCalorie.partition { it.name in majorNutrientsName }
    return Triple(calorie, major, minor)
}

@Composable
private fun NutrientScreenFail(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TitleMedium(
            text = stringResource(id = R.string.nutrient_fail),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(10f))
    }
}

@DarkPreview
@Composable
private fun NutrientScreenPreview_Success() {
    BlindarTheme {
        NutrientScreen(
            uiState = NutrientUiState.Success(
                date = Date.now(),
                mealTime = "중식",
                nutrients = listOf(
                    UiNutrient(name = "비타민A", amount = 595.2, unit = "R.E"),
                    UiNutrient(name = "티아민", amount = 0.5, unit = "mg"),
                    UiNutrient(name = "리보플라민", amount = 0.8, unit = "mg"),
                    UiNutrient(name = "비타민C", amount = 11.3, unit = "mg"),
                    UiNutrient(name = "탄수화물", amount = 110.2, unit = "g"),
                    UiNutrient(name = "단백질", amount = 34.9, unit = "g"),
                    UiNutrient(name = "열량", amount = 123.456, unit = "kcal"),
                    UiNutrient(name = "지방", amount = 32.0, unit = "g"),
                ),
            ),
            onNavigateToBack = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@DarkPreview
@Composable
private fun NutrientScreenPreview_Loading() {
    BlindarTheme {
        NutrientScreen(
            uiState = NutrientUiState.Loading(
                date = Date.now(),
                mealTime = "중식",
            ),
            onNavigateToBack = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@DarkPreview
@Composable
private fun NutrientScreenPreview_Fail() {
    BlindarTheme {
        NutrientScreen(
            uiState = NutrientUiState.Fail(
                date = Date.now(),
                mealTime = "석식",
            ),
            onNavigateToBack = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}