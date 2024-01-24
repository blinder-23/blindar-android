package com.practice.util


/**
 * 문자열에서 개행문자, 탭 문자, 공백 한 칸을 제거한다.
 */
fun String.removeWhitespaces(): String {
    return this.filter { it != '\n' && it != '\t' && it != ' ' }
}