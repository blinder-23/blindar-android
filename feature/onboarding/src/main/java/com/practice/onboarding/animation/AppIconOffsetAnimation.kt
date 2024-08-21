package com.practice.onboarding.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun getAppIconOffset(playAnimation: Boolean) =
    remember { Animatable(initialValue = if (playAnimation) initialAppIconOffset else targetAppIconOffset) }

internal suspend fun Animatable<Float, AnimationVector1D>.animateAppIconOffset() {
    animateTo(
        targetValue = targetAppIconOffset,
        animationSpec = tween(
            durationMillis = 1000,
            easing = CubicBezierEasing(0.5f, 0.0f, 0.5f, 1f),
        )
    )
}

private const val initialAppIconOffset = 0f
private const val targetAppIconOffset = -125f