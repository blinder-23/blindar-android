package com.practice.neis.meal.util

import com.google.gson.JsonObject
import com.practice.neis.common.NeisDeserializer
import com.practice.neis.common.getInt
import com.practice.neis.common.getString
import com.practice.neis.common.pojo.Header
import com.practice.neis.meal.pojo.*

class MealDeserializer : NeisDeserializer<MealModel, MealResponseModel> {
    override val dataKey: String = "mealServiceDietInfo"
    override val createResult: (Header, List<MealModel>) -> MealResponseModel = ::MealResponseModel
    override val exception: (String) -> Exception = ::MealDeserializerException

    override fun parseData(jsonObj: JsonObject): List<MealModel> {
        val rows = jsonObj.get("row").asJsonArray
        return rows.map { row ->
            with(row.asJsonObject) {
                MealModel(
                    officeCode = getString("ATPT_OFCDC_SC_CODE"),
                    officeName = getString("ATPT_OFCDC_SC_NM"),
                    schoolCode = getInt("SD_SCHUL_CODE"),
                    schoolName = getString("SCHUL_NM"),
                    mealCode = getInt("MMEAL_SC_CODE"),
                    mealName = getString("MMEAL_SC_NM"),
                    date = getString("MLSV_YMD"),
                    numberStudents = getInt("MLSV_FGR"),
                    menu = parseMenus(getString("DDISH_NM")),
                    originCountries = parseOrigins(getString("ORPLC_INFO")),
                    calorie = parseCalorie(getString("CAL_INFO")),
                    nutrients = parseNutrients(getString("NTR_INFO")),
                    fromDate = getString("MLSV_FROM_YMD"),
                    endDate = getString("MLSV_TO_YMD"),
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

fun String.splitBrAndTrim(): List<String> =
    split("<br/>")
        .map { it.trim() }
        .filter { it.isNotEmpty() }