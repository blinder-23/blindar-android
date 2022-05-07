package com.practice.hanbitlunch.api.meal.pojo

data class Menu(
    val name: String,
    val allergic: List<Int>
) {
    val allergicKorean: List<String>
        get() = allergic.map { allergicKoreanName[it - 1] }

    companion object {
        private val allergicKoreanName = listOf(
            "난류",
            "우유",
            "메밀",
            "땅콩",
            "대두",
            "밀",
            "고등어",
            "게",
            "새우",
            "돼지고기",
            "복숭아",
            "토마토",
            "아황산염",
            "호두",
            "닭고기",
            "쇠고기",
            "오징어",
            "조개류 (굴, 전복, 홍합 등)"
        )
    }
}

