package com.practice.api.memo.converter

import com.practice.api.memo.pojo.RemoteMemoEntity
import com.practice.domain.Memo
import com.practice.util.date.DateUtil

fun Memo.toRemote() = RemoteMemoEntity(
    id = id,
    userId = userId,
    date = DateUtil.toDateString(year, month, day),
    contents = content,
)