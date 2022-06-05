package com.practice.neis.common.pojo

data class ResultCode(
    val code: String,
    val message: String
) {
    companion object {
        private const val successCode = "INFO-000"
    }

    val success: Boolean = (code == successCode)
    val fail: Boolean = !success
}