package com.practice.neis.common.pojo

interface ResponseModel<T> {
    val header: Header
    val data: List<T>
}