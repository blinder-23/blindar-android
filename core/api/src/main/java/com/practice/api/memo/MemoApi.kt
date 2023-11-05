package com.practice.api.memo

import com.practice.api.BlindarRetrofit
import com.practice.api.memo.pojo.DeleteMemoResponse
import com.practice.api.memo.pojo.GetMemoResponse
import com.practice.api.memo.pojo.RemoteMemoEntity
import com.practice.api.memo.pojo.UpdateMemoResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MemoApi {
    @POST("memo")
    suspend fun updateMemo(
        @Body body: RemoteMemoEntity,
    ): UpdateMemoResponse

    @DELETE("memo/{memo_id}")
    suspend fun deleteMemo(
        @Path("memo_id", encoded = true) memoId: String
    ): DeleteMemoResponse

    @GET("memo")
    suspend fun getAllMemoOfUser(
        @Query("user_id") userId: String,
    ): GetMemoResponse
}

val memoApi: MemoApi
    get() = BlindarRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(MemoApi::class.java)