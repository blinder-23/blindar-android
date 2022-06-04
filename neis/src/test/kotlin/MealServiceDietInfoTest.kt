package meal

import com.practice.neis.NEISRetrofit
import com.practice.neis.meal.hanbitSchoolCode
import com.practice.neis.meal.seoulOfficeCode
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class MealServiceDietInfoTest {

    @Test
    fun testApi() = runBlocking {
        val api = NEISRetrofit.mealApi
        val result = api.getMealOfMonth(
            officeCode = seoulOfficeCode,
            schoolCode = hanbitSchoolCode,
            mealYearMonth = "202205",
            pageSize = 2
        )
        println(result)
    }

}