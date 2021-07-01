package ru.wintrade.mvp.model.api

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import ru.wintrade.mvp.model.entity.api.*

interface WinTradeApi {
    @GET("api/v1/trader/list/")
    fun getAllTraders(
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

    @POST("auth/token/logout/")
    fun logout(
        @Header("Authorization") token: String
    ): Completable

    @GET("auth/users/me/")
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Single<ResponseUserProfile>

    @POST("devices/")
    fun postDeviceToken(
        @Header("Authorization") token: String,
        @Body device: RequestDevice
    ): Single<RequestDevice>

    @GET("devices/")
    fun myDevices(
        @Header("Authorization") token: String
    ): Single<List<RequestDevice>>

    @POST("api/v1/subscription/create_observation/")
    fun observeToTrader(
        @Header("Authorization") token: String,
        @Body subscription: RequestSubscription
    ): Single<RequestSubscription>

    @DELETE("api/v1/subscription/{id_trader}/delete/")
    fun deleteObservation(
        @Header("Authorization") token: String,
        @Path(value = "id_trader", encoded = true) id: String
    ): Completable

    @POST("api/v1/subscription/create_subscription/")
    fun subscribeToTrader(
        @Header("Authorization") token: String,
        @Body subscription: RequestSubscription
    ): Single<RequestSubscription>

    @GET("api/v1/subscription/list/")
    fun mySubscriptions(
        @Header("Authorization") token: String
    ): Single<List<ResponseSubscription>>

    @GET("api/v1/subscription/trades/")
    fun subscriptionTrades(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseTrade>>

    @GET("api/v1/trader/my_trade/")
    fun getMyTrades(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseTrade>>

    @GET("api/v1/trader/posts/all/")
    fun getAllPosts(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponsePost>>

    @GET("api/v1/trader/posts/subscribed/")
    fun getPublisherPosts(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponsePost>>

    @GET("api/v1/trader/posts/trader/{trader_id}/")
    fun getTraderPosts(
        @Header("Authorization") token: String,
        @Path("trader_id") id: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponsePost>>

    @POST("auth/users/")
    fun signUp(
        @Body signUpData: RequestSignUp
    ): Single<ResponseSignUp>

    @POST("auth/users/reset_password/")
    fun resetPassword(
        @Body email: RequestResetPass
    ): Completable

//    @Multipart
//    @POST("api/v1/trader/post/create/")
//    fun createPost(
//        @Header("Authorization") token: String,
//        @Part("trader_id") trader_id: String,
//        @Part("text") text: String,
//        @Part("pinned") pinned: Boolean,
//        @Part images: List<MultipartBody.Part>?
//    ): Single<ResponsePost>

    @POST("api/v1/trader/post/create/")
    fun createPost(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Single<ResponsePost>

    @PUT("api/v1/trader/post/pinned/")
    fun updatePinnedPost(
        @Header("Authorization") token: String,
        @Body requestCreatePost: RequestCreatePost
    ): Single<ResponsePost>

    @FormUrlEncoded
    @PATCH("api/v1/trader/post/pinned/")
    fun updatePinnedPostPatch(
        @Header("Authorization") token: String,
        @Field("trader_id") id: String,
        @Field("text") text: String
    ): Completable

    @GET("api/v1/trader/post/pinned/")
    fun readPinnedPost(
        @Header("Authorization") token: String,
    ): Single<ResponsePost>

    @DELETE("api/v1/trader/post/pinned/")
    fun deletePinnedPost(
        @Header("Authorization") token: String
    ): Single<ResponsePost>

    @POST("api/v1/trader/like/post/{post_id}/")
    fun likePost(
        @Header("Authorization") token: String,
        @Path(value = "post_id", encoded = true) postId: Int
    ): Single<ResponseLikes>

    @POST("api/v1/trader/dislike/post/{post_id}/")
    fun dislikePost(
        @Header("Authorization") token: String,
        @Path(value = "post_id", encoded = true) postId: Int
    ): Single<ResponseLikes>

    @GET("api/v1/trader/posts/my/")
    fun getMyPosts(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponsePost>>

}