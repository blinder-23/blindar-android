package com.practice.util.date

/**
 * [days]를 밀리초 단위로 환산하여 더한 값을 반환한다. 이 함수는 타임스탬프 성격의 값에서만 호출되어야 한다.
 *
 * @param days 더할 날짜 수. [days]가 음수일 경우 날짜를 빼는 효과가 있다.
 */
fun Long.plusDays(days: Int): Long {
    val oneDayInMillis = 24 * 60 * 60 * 1000L
    return this + days * oneDayInMillis
}