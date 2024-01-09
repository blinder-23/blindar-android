package com.practice.main.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingMainScreen(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.1f)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
        )
    }
}