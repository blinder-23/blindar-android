package com.practice.hanbitlunch.api.meal

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.practice.hanbitlunch.api.meal.pojo.*
import java.lang.reflect.Type

class MealDeserializer : JsonDeserializer<MealResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MealResponse {
        val response = json?.asJsonObject?.get("mealServiceDietInfo")?.asJsonArray
            ?: throw MealDeserializerException("Json is $json")

        val headerObject = response[0].asJsonObject
        val header = parseHeader(headerObject)

        val rowObject = response[1].asJsonObject
        val row = parseMeals(rowObject)

        return MealResponse(
            header = header,
            mealData = row
        )
    }

    fun parseHeader(headJson: JsonObject): MealHeader {
        val heads = headJson.get("head").asJsonArray

        val listTotalCount = heads[0].getAsJsonObject("list_total_count").asInt

        val resultObj = heads[1].getAsJsonObject("RESULT").asJsonObject
        val resultCode = resultObj.get("CODE").asString
        val resultMessage = resultObj.get("MESSAGE").asString

        return MealHeader(
            listTotalCount = listTotalCount,
            resultCode = MealResultCode(
                code = resultCode,
                message = resultMessage
            )
        )
    }

    fun parseMeals(rowJson: JsonObject): List<Meal> {
        val rows = rowJson.get("row").asJsonArray
        return rows.map { row ->
            with(row.asJsonObject) {
                Meal(
                    officeCode = get("ATPT_OFCDC_SC_CODE").asString,
                    officeName = get("ATPT_OFCDC_SC_NM").asString,
                    schoolCode = get("SD_SCHUL_CODE").asInt,
                    schoolName = get("SCHUL_NM").asString,
                    mealCode = get("MMEAL_SC_CODE").asInt,
                    mealName = get("MMEAL_SC_NM").asString,
                    date = get("MLSV_YMD").asString,
                    numberStudents = get("MLSV_FGR").asInt,
                    menu = parseMenus(get("DDISH_NM").asString),
                    originCountries = get("ORPLC_INFO").asString.splitBrAndTrim(),
                    calorie = parseCalorie(get("CAL_INFO").asString),
                    nutrients = get("NTR_INFO").asString.splitBrAndTrim(),
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

    fun parseMenus(menusString: String): List<Menu> {
        val menuAllergic = menusString.splitBrAndTrim()
        return menuAllergic.map { parseMenu(it) }
    }

    private fun parseMenu(menuString: String): Menu {
        val split = menuString.split("(")
        val menuName = split[0].trim()
        val allergicNumbers = if (split.size > 1) {
            parseAllergic(split[1])
        } else {
            emptyList()
        }
        return Menu(
            name = menuName,
            allergic = allergicNumbers
        )
    }

    private fun parseAllergic(allergicString: String): List<Int> {
        val allergicNumberString = allergicString.replace("[^\\d]".toRegex(), " ").trim()
        val allergicToken = allergicNumberString.split(" ")
        return allergicToken.map { token -> token.toInt() }
    }

    fun parseOrigin(origins: String): List<Origin> {
        return origins.splitBrAndTrim().map { origin ->
            val tokens = origin.split(":").map { it.trim() }
            Origin(
                ingredient = tokens[0],
                originCountry = tokens[1]
            )
        }
    }

}

class MealDeserializerException(override val message: String) : Exception(message)

fun JsonElement.getAsJsonObject(key: String): JsonElement = this.asJsonObject.get(key)

fun String.splitBrAndTrim(): List<String> =
    split("<br/>")
        .map { it.trim() }
        .filter { it.isNotEmpty() }