package com.practice.hanbitlunch.screen

// TODO: hsk-ktx로 옮기기
/**
 * Divides a [Collection] into [divideCount] lists.
 *
 * @param divideCount Number of divided lists
 */
fun <T> Collection<T>.divide(divideCount: Int): List<List<T>> {
    val dividedListSize = this.size / divideCount
    val remainder = this.size % divideCount
    val sizes = (0 until divideCount).map {
        dividedListSize + (if (it < remainder) 1 else 0)
    }

    var lastIndex = 0
    val asList = this.toList()
    return sizes.map { size ->
        asList.subList(lastIndex, lastIndex + size).also {
            lastIndex += size
        }
    }
}