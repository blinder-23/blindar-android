package com.practice.api.user

import com.practice.api.BlindarRetrofit
import com.practice.api.user.pojo.RegisterCompleteResponse
import com.practice.api.user.pojo.RemoteUser
import com.practice.api.user.pojo.UserSchoolResponse
import com.practice.api.user.pojo.UserUpdateResponse
import com.practice.api.user.pojo.UsernameDuplicateResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("user/update")
    suspend fun updateUser(
        @Body body: RemoteUser,
    ): UserUpdateResponse

    /**
     * 사용자 이름이 이미 존재하는지 확인한다.
     *
     * @param username 중복 검사할 사용자 이름
     */
    @GET("user/duplicate")
    suspend fun checkUserDuplication(
        @Query("user_name") username: String,
    ): UsernameDuplicateResponse

    /**
     * 사용자의 가입 절차가 완료되었는지 확인한다.
     *
     * @param userId 확인할 사용자의 user id
     */
    @GET("user/check/{userId}")
    suspend fun checkUserRegisterCompleted(
        @Path("userId") userId: String,
    ): RegisterCompleteResponse

    @GET("get_my_school/{userId}")
    suspend fun getUserSchool(
        @Path("userId") userId: String,
    ): UserSchoolResponse
}

val userApi: UserApi
    get() = BlindarRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(UserApi::class.java)