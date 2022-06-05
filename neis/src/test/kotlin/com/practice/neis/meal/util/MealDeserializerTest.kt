package com.practice.neis.meal.util

import com.practice.neis.common.parseHeader
import com.practice.neis.common.pojo.Header
import com.practice.neis.common.pojo.ResultCode
import com.practice.neis.common.seoulOfficeCode
import com.practice.neis.common.toJson
import com.practice.neis.meal.pojo.MenuModel
import com.practice.neis.meal.pojo.NutrientModel
import com.practice.neis.meal.pojo.OriginModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import zipForEach


class MealDeserializerTest {

    private val deserializer = MealDeserializer()
    private val sampleJsonString = """
    {
        "mealServiceDietInfo": [
            {
                "head": [
                    {
                        "list_total_count": 20
                    },
                    {
                        "RESULT": {
                            "CODE": "INFO-000",
                            "MESSAGE": "정상 처리되었습니다."
                        }
                    }
                ]
            },
            {
                "row": [
                    {
                        "ATPT_OFCDC_SC_CODE": "B10",
                        "ATPT_OFCDC_SC_NM": "서울특별시교육청",
                        "SD_SCHUL_CODE": "7010578",
                        "SCHUL_NM": "한빛맹학교",
                        "MMEAL_SC_CODE": "2",
                        "MMEAL_SC_NM": "중식",
                        "MLSV_YMD": "20220502",
                        "MLSV_FGR": "167",
                        "DDISH_NM": "찹쌀밥  <br/>롤케이크  (1.2.5.6.)<br/>쇠고기미역국  (5.6.16.)<br/>오리불고기  (5.6.13.)<br/>포기김치  (9.)<br/>급식우유  (2.)<br/>마샐러드  ",
                        "ORPLC_INFO": "쌀 : 국내산<br/>김치류 : 국내산<br/>고춧가루(김치류) : 국내산<br/>쇠고기(종류) : 국내산(한우)<br/>돼지고기 : 국내산<br/>닭고기 : 국내산<br/>오리고기 : 국내산<br/>쇠고기 식육가공품 : 국내산<br/>돼지고기 식육가공품 : 국내산<br/>닭고기 식육가공품 : 국내산<br/>오리고기 가공품 : 국내산<br/>낙지 : 국내산<br/>고등어 : 국내산<br/>갈치 : 국내산<br/>오징어 : 국내산<br/>꽃게 : 국내산<br/>참조기 : 국내산<br/>콩 : 국내산",
                        "CAL_INFO": "1167.4 Kcal",
                        "NTR_INFO": "탄수화물(g) : 136.2<br/>단백질(g) : 37.9<br/>지방(g) : 54.1<br/>비타민A(R.E) : 449.9<br/>티아민(mg) : 0.6<br/>리보플라빈(mg) : 0.8<br/>비타민C(mg) : 16.5<br/>칼슘(mg) : 275.7<br/>철분(mg) : 5.6",
                        "MLSV_FROM_YMD": "20220502",
                        "MLSV_TO_YMD": "20220502"
                    }
                ]
            }
        ]
    }
    """
    private val sampleJson = sampleJsonString.toJson()

    @Test
    fun parseHeader() {
        val expected = Header(
            listTotalCount = 20,
            resultCode = ResultCode(
                code = "INFO-000",
                message = "정상 처리되었습니다."
            )
        )
        val headerElement = sampleJson.get("mealServiceDietInfo").asJsonArray[0].asJsonObject

        val actual = parseHeader(headerElement)
        assertEquals(expected, actual)
    }

    @Test
    fun parseMeal() {
        val rowElement = sampleJson.get("mealServiceDietInfo").asJsonArray[1].asJsonObject
        val actual = deserializer.parseMeals(rowElement)[0]
        println(actual)
        assertEquals(seoulOfficeCode, actual.officeCode)
    }

    @Test
    fun splitBrAndTrim() {
        val string = "찹쌀밥  <br/>롤케이크  <br/>"
        val expected = listOf("찹쌀밥", "롤케이크")
        val actual = string.splitBrAndTrim()
        assertEquals(expected, actual)
    }

    @Test
    fun parseCalorie() {
        val string = "1157.7 Kcal"
        val expected = 1157.7
        val actual = deserializer.parseCalorie(string)
        assertEquals(expected, actual, 1e-6)
    }

    @Test
    fun parseCalorie_Fail() {
        val wrongString = "1157.7"
        var exceptionOccur = false
        try {
            deserializer.parseCalorie(wrongString)
        } catch (e: MealDeserializerException) {
            exceptionOccur = true
        } finally {
            assert(exceptionOccur)
        }
    }

    @Test
    fun parseMenu() {
        val string = "롤케이크  (1.2.5.6.)<br/>오리불고기  (5.6.13.)"
        val expectedList = listOf(
            MenuModel(
                name = "롤케이크",
                allergic = listOf(1, 2, 5, 6)
            ),
            MenuModel(
                name = "오리불고기",
                allergic = listOf(5, 6, 13)
            )
        )
        val actualList = deserializer.parseMenus(string)
        expectedList.zipForEach(actualList) { expectedMenu, actualMenu ->
            assertEquals(expectedMenu, actualMenu)
        }
    }

    @Test
    fun parseOrigins() {
        val string = "쌀 : 국내산<br/>김치류 : 국내산<br/>"
        val expectedList = listOf(
            OriginModel(
                ingredient = "쌀",
                originCountry = "국내산",
            ),
            OriginModel(
                ingredient = "김치류",
                originCountry = "국내산",
            ),
        )
        val actualList = deserializer.parseOrigins(string)
        assertEquals(expectedList.size, actualList.size)
        expectedList.zipForEach(actualList) { expected, actual ->
            assertEquals(expected, actual)
        }
    }

    @Test
    fun parseNutrients() {
        val nutrients = "탄수화물(g) : 136.2<br/>단백질(g) : 37.9<br/>"
        val expectedList = listOf(
            NutrientModel(
                name = "탄수화물",
                unit = "g",
                amount = 136.2,
            ),
            NutrientModel(
                name = "단백질",
                unit = "g",
                amount = 37.9
            ),
        )
        val actualList = deserializer.parseNutrients(nutrients)
        assertEquals(expectedList.size, actualList.size)
        expectedList.zipForEach(actualList) { expected, actual ->
            assertEquals(expected, actual)
        }
    }
}