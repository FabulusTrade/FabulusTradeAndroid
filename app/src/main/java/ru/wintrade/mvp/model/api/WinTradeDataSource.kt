package ru.wintrade.mvp.model.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import ru.wintrade.mvp.model.entity.api.*

interface WinTradeDataSource {
    @GET("api/v1/trader/list/")
    fun getAllTraders(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseTrader>>

    @GET("api/v1/trader/{trader_id}/")
    fun getTraderById(
        @Header("Authorization") token: String,
        @Path(value = "trader_id", encoded = true) traderId: Long
    ): Single<ResponseTrader>

    @GET("api/v1/trader/{trader_id}/trade/")
    fun getTradesByTrader(
        @Header("Authorization") token: String,
        @Path(value = "trader_id", encoded = true) traderId: Long,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseTrade>>

    @GET("api/v1/trader/{trader_id}/trade/{trade_id}/")
    fun getTradeById(
        @Header("Authorization") token: String,
        @Path(value = "trader_id", encoded = true) traderId: Long,
        @Path(value = "trade_id", encoded = true) tradeId: Long
    ): Single<ResponseTrade>

    @POST("auth/token/login/")
    fun auth(
        @Body requestAuth: RequestAuth
    ): Single<ResponseAuth>

    @GET("auth/users/me/")
    fun getProfile(
        @Header("Authorization") token: String
    ): Single<ResponseProfile>

    @POST("devices/")
    fun postDeviceToken(
        @Header("Authorization") token: String,
        @Body device: RequestDevice
    ): Single<RequestDevice>

    @GET("devices/")
    fun myDevices(
        @Header("Authorization") token: String
    ): Single<List<RequestDevice>>

    @POST("api/v1/subscription/create_subscription/")
    fun subscribeToTrader(
        @Header("Authorization") token: String,
        @Body subscription: RequestSubscription
    ): Single<RequestSubscription>

    @GET("api/v1/subscription/list/")
    fun mySubscriptions(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseSubscription>>

    @GET("api/v1/trader/posts/all/")
    fun getAllPosts(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponsePost>>

    @POST("auth/users/")
    fun signUp(
        @Body signUpData: RequestSignUp
    ): Single<ResponseSignUp>
}