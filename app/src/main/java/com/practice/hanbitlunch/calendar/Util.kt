package com.practice.hanbitlunch.calendar

import java.time.LocalDate

val LocalDate.clickLabel: String
    get() = "${monthValue}월 ${dayOfMonth}일"