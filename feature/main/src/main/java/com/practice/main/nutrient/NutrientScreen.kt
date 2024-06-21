package com.practice.main.nutrient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hsk.ktx.date.Date

@Composable
fun NutrientScreen(
    route: NutrientRoute,
    modifier: Modifier = Modifier,
) {
    val date = Date(route.year, route.month, route.dayOfMonth)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = date.toString(),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = route.schoolCode.toString(),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}