package com.practice.hanbitlunch

import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable

@Serializable
sealed class BlindarRoute(private val order: Int) : Comparable<BlindarRoute> {
    internal val route: String
        get() = this::class.java.canonicalName?.toString().toString()

    override fun compareTo(other: BlindarRoute): Int {
        return this.order.compareTo(other.order)
    }

    @Serializable
    data object Splash : BlindarRoute(0)

    @Serializable
    data object Onboarding : BlindarRoute(1)

    @Serializable
    data object VerifyPhone : BlindarRoute(2)

    @Serializable
    data object VerifyUsername : BlindarRoute(3)

    @Serializable
    data object SelectSchool : BlindarRoute(4)

    @Serializable
    data object Main : BlindarRoute(5)

    @Serializable
    data object Settings : BlindarRoute(6)

    // TODO: add memo, schedule routes

    companion object {
        // TODO: 왜 Splash만 null로 들어감?
        private val entries =
            listOf(Splash, Onboarding, VerifyPhone, VerifyUsername, SelectSchool, Main, Settings)

//        fun NavBackStackEntry.toBlindarRoute() =
//            entries.firstOrNull {
//                Log.d("BlindarRoute", "entries: $entries")
//                Log.d("BlindarRoute", "entry: ${it == null}, this: $this")
//                it.route == this.destination.route.orEmpty()
//            } ?: throw IllegalArgumentException("Unknown route: ${this.destination.route}")

        fun NavBackStackEntry.toBlindarRoute(): BlindarRoute = when (this.destination.route) {
            Splash.route -> Splash
            Onboarding.route -> Onboarding
            VerifyPhone.route -> VerifyPhone
            VerifyUsername.route -> VerifyUsername
            SelectSchool.route -> SelectSchool
            Main.route -> Main
            else -> throw IllegalArgumentException("Unknown route: ${this.destination.route}")
        }
    }
}