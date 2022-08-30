package com.practice.neis.common.pojo

data class ResultCode(
    val type: String,
    val code: String,
    val message: String
) {
    companion object {
        private const val successCode = "000"
    }

    val success: Boolean = (code == successCode)
    val fail: Boolean = !success
}