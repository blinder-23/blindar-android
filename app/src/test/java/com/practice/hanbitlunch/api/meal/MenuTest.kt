package com.practice.hanbitlunch.api.meal

import com.practice.hanbitlunch.api.meal.pojo.Menu
import org.junit.Assert.assertEquals
import org.junit.Test

class MenuTest {

    @Test
    fun testAllergicKorean() {
        val allergic = listOf(1, 3, 8)
        val expected = listOf("난류", "메밀", "게")
        val actual = Menu(name = "test", allergic = allergic).allergicKorean
        assertEquals(expected, actual)
    }
}