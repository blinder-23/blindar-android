package com.practice.util

import com.practice.util.date.plusDays
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DateUtilExtTest {
    @ValueSource(ints = [0, 1, -1])
    @ParameterizedTest
    fun plusDaysTest(days: Int) {
        val nowInMillis = System.currentTimeMillis()
        assertThat(nowInMillis.plusDays(days)).isEqualTo(nowInMillis + 86400 * 1000L * days)
    }
}