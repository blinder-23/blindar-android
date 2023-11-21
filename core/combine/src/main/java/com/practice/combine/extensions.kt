package com.practice.combine

import com.hsk.ktx.date.Date
import com.practice.api.meal.pojo.MealModel
import com.practice.api.meal.pojo.MenuModel
import com.practice.api.meal.pojo.NutrientModel
import com.practice.api.meal.pojo.OriginModel
import com.practice.api.schedule.pojo.ScheduleModel
import com.practice.api.toEpochDate
import com.practice.domain.Memo
import com.practice.domain.meal.Meal
import com.practice.domain.meal.Menu
import com.practice.domain.meal.Nutrient
import com.practice.domain.meal.Origin
import com.practice.domain.schedule.Schedule
import java.security.MessageDigest

fun MealModel.toMealEntity() = Meal(
    schoolCode = 7010578,
    year = ymd.substring(0..3).toInt(),
    month = ymd.substring(4..5).toInt(),
    day = ymd.substring(6..7).toInt(),
    menus = dishes.map { it.toMenuEntity() },
    origins = origins.map { it.toOriginEntity() },
    nutrients = nutrients.map { it.toNutrientEntity() },
    calorie = calorie,
)

fun MenuModel.toMenuEntity() = Menu(
    name = menu,
    allergies = allergies,
)

fun OriginModel.toOriginEntity() = Origin(
    ingredient = ingredient,
    origin = origin,
)

fun NutrientModel.toNutrientEntity() = Nutrient(
    name = nutrient,
    unit = unit,
    amount = amount.toDouble(),
)

fun ScheduleModel.toScheduleEntity(): Schedule {
    val date = date.toEpochDate(hourDiff = 9)
    return Schedule(
        schoolCode = schoolCode,
        id = id,
        year = date.year,
        month = date.month,
        day = date.dayOfMonth,
        eventName = title,
        eventContent = contents
    )
}

fun Memo.createId(): String {
    val currentTimeEpoch = Date.now().toEpochSecond()
    return listOf(id, userId, currentTimeEpoch, content).joinToString { it.toString() }.sha()
}

fun String.sha(algorithm: String = "SHA-512"): String {
    val digest = MessageDigest.getInstance(algorithm)
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("") { str, it -> str + "%02x".format(it) }
}

internal val TAG = "domain"