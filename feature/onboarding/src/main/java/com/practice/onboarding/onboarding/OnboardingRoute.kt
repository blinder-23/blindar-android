package com.practice.onboarding.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class OnboardingRoute(
    val playAnimation: Boolean = false,
)
