package com.practice.api.memo.converter

import com.practice.api.memo.pojo.RemoteMemoEntity
import com.practice.domain.Memo

fun RemoteMemoEntity.toMemo() = Memo(
    id = id,
    userId = userId,
    year = date.substring(0..3).toInt(),
    month = date.substring(4..5).toInt(),
    day = date.substring(6..7).toInt(),
    content = contents,
)