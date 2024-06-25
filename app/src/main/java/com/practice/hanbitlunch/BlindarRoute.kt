package com.practice.hanbitlunch

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.serialization.decodeArguments
import com.practice.main.memo.MemoRoute
import com.practice.main.nutrient.NutrientRoute
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable
data object SplashRoute

@Serializable
data object OnboardingRoute

@Serializable
data object VerifyPhoneRoute

@Serializable
data object VerifyUsernameRoute

@Serializable
data object SelectSchoolRoute

@Serializable
data object MainRoute

@Serializable
data object SettingsRoute

/**
 * 매개변수가 없는 route는 여기에 data object로 선언하고,
 * 매개변수가 있는 route는 각 모듈에 data class로 선언한다.
 * 그 후 [blindarRoute]에 KClass를 추가한다.
 */
private val blindarRoutes = listOf(
    SplashRoute::class,
    OnboardingRoute::class,
    VerifyPhoneRoute::class,
    VerifyUsernameRoute::class,
    SelectSchoolRoute::class,
    MainRoute::class,
    SettingsRoute::class,
    NutrientRoute::class,
    MemoRoute::class,
).associateBy {
    it.simpleName
}.toPersistentHashMap()

/**
 * Only call this when the type of the route is not known. I.e. in NavHost::enterTransition().
 *
 * In many cases, type-safe [NavBackStackEntry.toRoute()] is recommended.
 */
@OptIn(InternalSerializationApi::class)
fun NavBackStackEntry.toBlindarRoute(): Any {
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