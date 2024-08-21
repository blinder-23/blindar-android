package com.practice.onboarding.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun getButtonAlpha(playAnimation: Boolean) =
    remember { Animatable(if (playAnimation) initialButtonAlpha else targetButtonAlpha) }

internal suspend fun Animatable<Float, AnimationVector1D>.animateButtonAlpha() {
    animateTo(
        targetValue = targetButtonAlpha,
        animationSpec = tween(
            durationMillis = 500,
        ),
    )
}


private const val initialButtonAlpha = 0f
private const val targetButtonAlpha = 1f