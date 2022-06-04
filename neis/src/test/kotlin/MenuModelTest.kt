package meal

import com.practice.neis.meal.pojo.MenuModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MenuModelTest {

    @Test
    fun testAllergicKorean() {
        val allergic = listOf(1, 3, 8)
        val expected = listOf("난류", "메밀", "게")
        val actual = MenuModel(name = "test", allergic = allergic).allergicKorean
        assertEquals(expected, actual)
    }
}