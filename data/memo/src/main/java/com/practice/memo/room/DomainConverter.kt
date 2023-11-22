package com.practice.memo.room

import com.practice.domain.Memo

fun MemoEntity.toMemo() = Memo(
    id = id,
    userId = userId,
    year = date.slice(0..3).toInt(),
    month = date.slice(4..5).toInt(),
    day = date.slice(6..7).toInt(),
    content = content,
    isSavedOnRemote = isSavedOnRemote,
)

fun Memo.toEntity(): MemoEntity {
    val monthPadStartZero = month.toString().padStart(2, '0')
    val dayPadStartZero = day.toString().padStart(2, '0')
    return MemoEntity(
        id = id,
        userId = userId,
        date = "$year$monthPadStartZero$dayPadStartZero",
        content = content,
        isSavedOnRemote = isSavedOnRemote,
    )
}

fun List<MemoEntity>.toMemo() = map { it.toMemo() }