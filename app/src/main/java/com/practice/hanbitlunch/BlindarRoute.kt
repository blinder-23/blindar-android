package com.practice.hanbitlunch

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.serialization.decodeArguments
import com.practice.main.nutrient.NutrientRoute
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable
sealed interface BlindarRoute {
    @Serializable
    data object Splash : BlindarRoute

    @Serializable
    data object Onboarding : BlindarRoute

    @Serializable
    data object VerifyPhone : BlindarRoute

    @Serializable
    data object VerifyUsername : BlindarRoute

    @Serializable
    data object SelectSchool : BlindarRoute

    @Serializable
    data object Main : BlindarRoute

    @Serializable
    data object Settings : BlindarRoute

    @Serializable
    data class Nutrient(
        override val year: Int,
        override val month: Int,
        override val dayOfMonth: Int,
        override val schoolCode: Int,
        override val mealTime: String,
    ) : BlindarRoute, NutrientRoute

    // TODO: add memo, schedule routes
}

private val blindarRoutes = listOf(
    BlindarRoute.Splash::class,
    BlindarRoute.Onboarding::class,
    BlindarRoute.VerifyPhone::class,
    BlindarRoute.VerifyUsername::class,
    BlindarRoute.SelectSchool::class,
    BlindarRoute.Main::class,
    BlindarRoute.Settings::class,
    BlindarRoute.Nutrient::class,
).associateBy {
    it.simpleName
}.toPersistentHashMap()

@OptIn(InternalSerializationApi::class)
fun NavBackStackEntry.toBlindarRoute(): BlindarRoute {
    val clazz = blindarRoutes[destinationClassName]
        ?: throw IllegalArgumentException("No route with name: $destinationClassPackageName")

    val bundle = arguments ?: Bundle()
    val typeMap = destination.arguments.mapValues { it.value.type }
    return clazz.serializer().decodeArguments(bundle, typeMap)
}

private val NavBackStackEntry.destinationClassPackageName: String?
    get() = this.destination.route?.substringBefore("/")

private val NavBackStackEntry.destinationClassName: String
    get() = destinationClassPackageName?.substringAfterLast(".").orEmpty()