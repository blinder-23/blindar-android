package com.practice.util

import com.practice.util.date.DateUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DateUtilTest {
    @Test
    fun `월, 일이 모두 2자리 정수일 때`() {
        assertThat(DateUtil.toDateString(2023, 11, 12))
            .isEqualTo("20231112")
    }

    @Test
    fun `월이 1자리 정수일 때`() {
        assertThat(DateUtil.toDateString(2023, 9, 12))
            .isEqualTo("20230912")
    }

    @Test
    fun `일이 1자리 정수일 때`() {
        assertThat(DateUtil.toDateString(2023, 10, 1))
            .isEqualTo("20231001")
    }
}