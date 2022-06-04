package meal

import com.practice.neis.meal.MealDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MealDataSourceTest {

    private val mealDataSource = MealDataSource()

    @Test
    fun getMeal() = runTest {
        val result = mealDataSource.getMeal(2022, 5)
        println(result)
    }

}