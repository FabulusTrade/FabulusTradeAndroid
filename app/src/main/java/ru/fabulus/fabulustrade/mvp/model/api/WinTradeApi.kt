package ru.fabulus.fabulustrade.mvp.model.api

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import ru.fabulus.fabulustrade.mvp.model.entity.api.*

/**
 * http://wintrade.fun/redoc/
 */
interface WinTradeApi {
    @GET("api/v1/trader/list/sort/profit/")
    fun getTradersProfitFiltered(
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseTrader>>

    @GET("api/v1/trader/list/sort/followers")
    fun getTradersFollowersFiltered(
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseTrader>>

    @GET("api/v1/trader/{trader_id}/")
    fun getTraderById(
        @Header("Authorization") token: String,
        @Path(value = "trader_id", encoded = true) traderId: String
    ): Single<ResponseTrader>

    @GET("api/v1/trader/{trader_id}/trade/")
    fun getTradesByTrader(
        @Header("Authorization") token: String,
        @Path(value = "trader_id", encoded = true) traderId: Long,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseTrade>>

    @GET("api/v1/trader/trade/{trade_id}/")
    fun getTradeById(
        @Header("Authorization") token: String,
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

    /**
     * Получение постов от тех на кого подписан и от тех за кем просто наблюдаешь
     */
    @GET("api/v1/trader/posts/following/")
    fun getPostsFollowerAndObserving(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("flashed_posts_only") flashedPostsOnly: Boolean
    ): Single<ResponsePagination<ResponsePost>>

    @GET("/api/v1/trader/posts/all/")
    fun getAllPosts(
        @Header("Authorization") token: String?,
        @Query("page") page: Int = 1,
        @Query("flashed_posts_only") flashedPostsOnly: Boolean
    ):  Single<ResponsePagination<ResponsePost>>

    @GET("api/v1/trader/{trader_id}/aggregated/")
    fun getAggregatedTrades(
        @Header("Authorization") token: String,
        @Path("trader_id") id: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseAggregatedTrade>>

    @GET("api/v1/trader/post/by_trade/{trade_id}/")
    fun getArgumentByTrade(
        @Header("Authorization") token: String,
        @Path("trade_id") id: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseArgument>>

    @GET("api/v1/trader/{trader_id}/by_company/{company_id}/")
    fun getDealsByCompany(
        @Header("Authorization") token: String,
        @Path("trader_id") id: String,
        @Path("company_id") companyId: Int,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseCompanyTradingOperations>>

    @GET("api/v1/trader/my_trade_detailed/by_company/{company_id}/")
    fun getDealsJournalByCompany(
        @Header("Authorization") token: String,
        @Path("company_id") companyId: Int,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseCompanyTradingOperationsJournal>>

    @POST("auth/users/")
    fun signUp(
        @Body signUpData: RequestSignUp
    ): Single<ResponseSignUp>

    @POST("auth/users/")
    fun signUpAsTrader(
        @Body signUpData: RequestSignUpAsTrader
    ): Completable

    @POST("auth/users/reset_password/")
    fun resetPassword(
        @Body email: RequestResetPass
    ): Completable

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

    @PATCH("api/v1/trader/post/{id}/")
    @Multipart
    fun updatePublication(
        @Header("Authorization") token: String,
        @Path("id") postId: String,
        @Part id: MultipartBody.Part,
        @Part text: MultipartBody.Part,
        @Part imagesToAdd: List<MultipartBody.Part>
    ): Single<ResponsePost>

    @PATCH("api/v1/trader/post/{id}/")
    @Multipart
    fun updateArgument(
        @Header("Authorization") token: String,
        @Path("id") postId: String,
        @Part id: MultipartBody.Part,
        @Part text: MultipartBody.Part,
        @Part imagesToAdd: List<MultipartBody.Part>,
        @Part stopLoss: MultipartBody.Part?,
        @Part takeProfit: MultipartBody.Part?,
        @Part dealTerm: MultipartBody.Part?,
    ): Single<ResponseArgument>

    @POST("auth/avatar/")
    @Multipart
    fun changeAvatar(
        @Header("Authorization") token: String,
        @Part avatar: MultipartBody.Part
    ): Completable

    @DELETE("auth/avatar/")
    fun deleteAvatar(
        @Header("Authorization") token: String,
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

    @DELETE("api/v1/trader/post/{id}/")
    fun deletePost(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) postId: Int
    ): Completable

    @DELETE("api/v1/trader/post/{id}/image/{fileName}")
    fun deleteImageInPost(
        @Header("Authorization") token: String,
        @Path(value = "id") postId: String,
        @Path(value = "fileName") fileName: String
    ): Completable

    @GET("api/v1/trader/posts/my/")
    fun getMyPosts(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("flashed_posts_only") flashedPostsOnly: Boolean = false
    ): Single<ResponsePagination<ResponsePost>>

    @POST("api/v1/feedback/send/")
    fun sendQuestion(
        @Header("Authorization") token: String,
        @Body message: RequestQuestion
    ): Completable

    @GET("api/v1/trader/statistics/{trader_id}/")
    fun getTraderStatistic(
        @Path(value = "trader_id", encoded = true) traderId: String,
        @Query("page") page: Int = 1
    ): Single<ResponseTraderStatistic>

    @PATCH("api/v1/trader/{trader_id}/ ")
    fun updateTraderRegistration(
        @Header("Authorization") token: String,
        @Path("trader_id", encoded = true) traderId: String,
        @Body registrationInfo: RequestTraderRegistrationInfo
    ): Completable

    @FormUrlEncoded
    @POST("/auth/username_exists/")
    fun checkUsername(
        @Field("username", encoded = true) username: String,
        @Field("email", encoded = true) email: String,
    ): Completable

    @FormUrlEncoded
    @POST("/api/v1/trader/number_of_trades/")
    fun getTraderOperationsCount(
        @Field(value = "uuid") uuidTrader: String
    ): Single<ResponseOperationsCount>

    // добавление комментария к посту
    // если задан parent_comment - то добавляется комментарий к комментарию с id = parent_comment
    // внутри поста
    @FormUrlEncoded
    @POST("/api/v1/trader/comment/create/")
    fun addPostComment(
        @Header("Authorization") token: String,
        @Field("post") postId: Int,
        @Field("text", encoded = true) text: String,
        @Field("parent_comment") parentCommentId: Long?
    ): Single<ResponseComment>

    // обновление текста комментария
    @FormUrlEncoded
    @PATCH("/api/v1/trader/comment/{comment_id}/update/")
    fun updateComment(
        @Header("Authorization") token: String,
        @Path(value = "comment_id") commentId: Long,
        @Field("text", encoded = true) text: String
    ): Single<ResponseComment>

    @POST("api/v1/trader/like/comment/{comment_id}/")
    fun likeComment(
        @Header("Authorization") token: String,
        @Path(value = "comment_id", encoded = true) commentId: Long
    ): Single<ResponseLikes>

    // счетчик количества репостов
    @GET("api/v1/trader/post/{post_id}/repost")
    fun incRepostCount(
        @Path(value = "post_id") postId: Int
    ): Single<ResponseIncRepostCount>

    @DELETE("api/v1/trader/comment/{comment_id}/delete/")
    fun deleteComment(
        @Header("Authorization") token: String,
        @Path(value = "comment_id") commentId: Long
    ): Single<ResponseDeleteComment>

    //список жалоб
    @GET("api/v1/trader/complaint/")
    fun getComplaints(
        @Header("Authorization") token: String
    ): Single<ResponseComplaints>

    @FormUrlEncoded
    @PATCH("api/v1/trader/posts/{post_id}/complaint/")
    fun complaintOnPost(
        @Header("Authorization") token: String,
        @Path(value = "post_id") postId: Int,
        @Field("complaint") complaintId: Int
    ): Completable

    @FormUrlEncoded
    @PATCH("api/v1/trader/comment/{comment_id}/complaint/")
    fun complaintOnComment(
        @Header("Authorization") token: String,
        @Path(value = "comment_id") commentId: Long,
        @Field("complaint") complaintId: Int
    ): Completable

    @FormUrlEncoded
    @PATCH("api/v1/trader/post/{post_id}/")
    fun setFlashedPost(
        @Header("Authorization") token: String,
        @Path(value = "post_id") postId: Int,
        @Field("is_flashed") isFlashed: Boolean
    ): Single<ResponseSetFlashedPost>

    //разрешения на добавление поста, комментария
    @GET("api/v1/trader/block/me/")
    fun getBlockUserInfo(
        @Header("Authorization") token: String
    ): Single<ResponseBlockUserInfo>

    //запрещено ли добавлять комментарии к посту
    @GET("api/v1/trader/post/{post_id}/blocked_users/")
    fun getCommentBlockedUsers(
        @Header("Authorization") token: String,
        @Path(value = "post_id") postId: Int
    ): Single<ResponseBlockUserComments>

    // блокировка добавления комменатирев к своим постам
    @POST("api/v1/trader/block_comments/")
    fun blockUserComments(
        @Header("Authorization") token: String,
        @Body body: RequestBlockUserComments
    ): Single<ResponseBlockCommentUser>

    // разблокировка добавления комменатирев к своим постам
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/v1/trader/block_comments/", hasBody = true)
    fun unblockUserComments(
        @Header("Authorization") token: String,
        @Field("blockedUserID") userID: String
    ): Single<ResponseUnblockCommentUser>

    // добавление в чёрный список
    @FormUrlEncoded
    @POST("api/v1/trader/blacklist/update/")
    fun addToBlacklist(
        @Header("Authorization") token: String,
        @Field("user_in_blacklist_id") traderID: String
    ): Single<ResponseAddToBlacklist>

    // удаление из чёрного списка
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/v1/trader/blacklist/update/", hasBody = true)
    fun deleteFromBlacklist(
        @Header("Authorization") token: String,
        @Field("user_in_blacklist_id") traderID: String
    ): Single<ResponseAddToBlacklist>

    // получение черного списка
    @GET("api/v1/trader/blacklist/")
    fun getBlacklist(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Single<ResponsePagination<ResponseBlacklistItem>>
}