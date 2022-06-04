package com.practice.neis.meal.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.practice.neis.common.parseHeader
import com.practice.neis.meal.pojo.*
import java.lang.reflect.Type

class MealDeserializer : JsonDeserializer<MealResponseModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MealResponseModel {
        val response = json?.asJsonObject?.get("mealServiceDietInfo")?.asJsonArray
            ?: throw MealDeserializerException("Json is $json")

        val headerObject = response[0].asJsonObject
        val header = parseHeader(headerObject)

        val rowObject = response[1].asJsonObject
        val row = parseMeals(rowObject)

        return MealResponseModel(
            header = header,
            mealData = row
        )
    }

    fun parseMeals(rowJson: JsonObject): List<MealModel> {
        val rows = rowJson.get("row").asJsonArray
        return rows.map { row ->
            with(row.asJsonObject) {
                MealModel(
                    officeCode = get("ATPT_OFCDC_SC_CODE").asString,
                    officeName = get("ATPT_OFCDC_SC_NM").asString,
                    schoolCode = get("SD_SCHUL_CODE").asInt,
                    schoolName = get("SCHUL_NM").asString,
                    mealCode = get("MMEAL_SC_CODE").asInt,
                    mealName = get("MMEAL_SC_NM").asString,
                    date = get("MLSV_YMD").asString,
                    numberStudents = get("MLSV_FGR").asInt,
                    menu = parseMenus(get("DDISH_NM").asString),
                    originCountries = parseOrigins(get("ORPLC_INFO").asString),
                    calorie = parseCalorie(get("CAL_INFO").asString),
                    nutrients = parseNutrients(get("NTR_INFO").asString),
                    fromDate = get("MLSV_FROM_YMD").asString,
                    endDate = get("MLSV_TO_YMD").asString
                )
            }
        }
    }

    fun parseCalorie(calorieString: String): Double {
        if (!calorieString.contains("Kcal")) throw MealDeserializerException("Calorie string doesn't contain \"Kcal\".")
        return calorieString.split(" ")[0].toDouble()
    }

    fun parseMenus(menusString: String): List<MenuModel> {
        val menuAllergic = menusString.splitBrAndTrim()
        return menuAllergic.map { parseMenu(it) }
    }

    private fun parseMenu(menuString: String): MenuModel {
        val split = menuString.split("(")
        val menuName = split[0].trim()
        val allergicNumbers = if (split.size > 1) {
            parseAllergic(split[1])
        } else {
            emptyList()
        }
        return MenuModel(
            name = menuName,
            allergic = allergicNumbers
        )
    }

    private fun parseAllergic(allergicString: String): List<Int> {
        val allergicNumberString = allergicString.replace("[^\\d]".toRegex(), " ").trim()
        val allergicToken = allergicNumberString.split(" ")
        return allergicToken.map { token -> token.toInt() }
    }

    fun parseOrigins(origins: String): List<OriginModel> {
        return origins.splitBrAndTrim().map { origin ->
            parseOrigin(origin)
        }
    }

    private fun parseOrigin(origin: String): OriginModel {
        val tokens = origin.split(":").map { it.trim() }
        return OriginModel(
            ingredient = tokens[0],
            originCountry = tokens[1]
        )
    }

    fun parseNutrients(nutrients: String): List<NutrientModel> {
        return nutrients.splitBrAndTrim().map { nutrient ->
            parseNutrient(nutrient)
        }
    }

    private fun parseNutrient(nutrient: String): NutrientModel {
        val nutrientBracketRemoved = nutrient.replace("[(): ]".toRegex(), " ")
        val tokens = nutrientBracketRemoved.split(" ")
        return NutrientModel(
            name = tokens[0],
            unit = tokens[1],
            amount = tokens.last().toDouble()
        )
    }
}

class MealDeserializerException(override val message: String) : Exception(message)

fun JsonElement.getAsJsonObject(key: String): JsonElement = this.asJsonObject.get(key)

fun String.splitBrAndTrim(): List<String> =
    split("<br/>")
        .map { it.trim() }
        .filter { it.isNotEmpty() }