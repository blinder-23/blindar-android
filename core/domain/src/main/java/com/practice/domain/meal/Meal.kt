package com.practice.domain.meal

data class Meal(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val menus: List<Menu>,
    val origins: List<Origin>,
    val calorie: Double,
    val nutrients: List<Nutrient>,
) {
    /**
     * 모든 메뉴의 이름을 쉼표로 구분된 문자열로 결합합니다.
     */
    val concatenatedMenu: String
        get() = menus.joinToString(", ") { it.name }
}