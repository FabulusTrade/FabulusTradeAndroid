package ru.fabulus.fabulustrade.mvp.model.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.MultipartBody
import ru.fabulus.fabulustrade.mvp.model.api.WinTradeApi
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.BlacklistItem
import ru.fabulus.fabulustrade.mvp.model.entity.BlockUserInfo
import ru.fabulus.fabulustrade.mvp.model.entity.Comment
import ru.fabulus.fabulustrade.mvp.model.entity.CommentBlockedUser
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.DeleteCommentResult
import ru.fabulus.fabulustrade.mvp.model.entity.IncPostResult
import ru.fabulus.fabulustrade.mvp.model.entity.JournalTradesSortedByCompany
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.ResultAddToBlacklist
import ru.fabulus.fabulustrade.mvp.model.entity.ResultBlockUserComment
import ru.fabulus.fabulustrade.mvp.model.entity.ResultUnblockUserComment
import ru.fabulus.fabulustrade.mvp.model.entity.Subscription
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.model.entity.TradesByCompanyAggregated
import ru.fabulus.fabulustrade.mvp.model.entity.TradesSortedByCompany
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestAuth
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestBlockUserComments
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestCreatePost
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestDevice
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestQuestion
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestResetPass
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestSignUp
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestSignUpAsTrader
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestSubscription
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestTraderRegistrationInfo
import ru.fabulus.fabulustrade.mvp.model.entity.api.ResponseAddToBlacklist
import ru.fabulus.fabulustrade.mvp.model.entity.api.ResponseSetFlashedPost
import ru.fabulus.fabulustrade.mvp.model.entity.api.ResponseSignUp
import ru.fabulus.fabulustrade.mvp.model.entity.api.ResponseUserProfile
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import ru.fabulus.fabulustrade.mvp.model.entity.exception.NoInternetException
import ru.fabulus.fabulustrade.mvp.model.network.NetworkStatus
import ru.fabulus.fabulustrade.util.mapToAggregatedTrade
import ru.fabulus.fabulustrade.util.mapToArgument
import ru.fabulus.fabulustrade.util.mapToBlacklistItem
import ru.fabulus.fabulustrade.util.mapToBlockUserInfo
import ru.fabulus.fabulustrade.util.mapToComment
import ru.fabulus.fabulustrade.util.mapToCommentBlockUserInfo
import ru.fabulus.fabulustrade.util.mapToComplaints
import ru.fabulus.fabulustrade.util.mapToDeleteCommentResult
import ru.fabulus.fabulustrade.util.mapToIncPostResult
import ru.fabulus.fabulustrade.util.mapToMultipartBodyPart
import ru.fabulus.fabulustrade.util.mapToPagination
import ru.fabulus.fabulustrade.util.mapToPost
import ru.fabulus.fabulustrade.util.mapToResultAddToBlacklist
import ru.fabulus.fabulustrade.util.mapToResultBlockUserComment
import ru.fabulus.fabulustrade.util.mapToResultUnblockUserComment
import ru.fabulus.fabulustrade.util.mapToSubscription
import ru.fabulus.fabulustrade.util.mapToTrade
import ru.fabulus.fabulustrade.util.mapToTradeByCompany
import ru.fabulus.fabulustrade.util.mapToTradeJournalByCompany
import ru.fabulus.fabulustrade.util.mapToTrader
import ru.fabulus.fabulustrade.util.mapToTraderStatistic

class ApiRepo(val api: WinTradeApi, val networkStatus: NetworkStatus) {

    val newTradeSubject = PublishSubject.create<Boolean>()

    fun getTradersProfitFiltered(page: Int = 1): Single<Pagination<Trader>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getTradersProfitFiltered(page)
                        .flatMap { respPag ->
                            val traders =
                                respPag.results.map { apiTrader -> mapToTrader(apiTrader) }
                            Single.just(mapToPagination(respPag, traders))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTradersFollowersFiltered(page: Int = 1): Single<Pagination<Trader>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getTradersFollowersFiltered(page)
                        .flatMap { respPag ->
                            val traders =
                                respPag.results.map { apiTrader -> mapToTrader(apiTrader) }
                            Single.just(mapToPagination(respPag, traders))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTraderStatistic(
        traderId: String,
    ): Single<TraderStatistic> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getTraderStatistic(traderId)
                        .flatMap { statistic ->
                            Single.just(mapToTraderStatistic(statistic))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTraderById(token: String, traderId: String): Single<Trader> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getTraderById(token, traderId)
                        .flatMap { response ->
                            val trader = mapToTrader(response)
                            Single.just(trader)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTradesByTrader(token: String, traderId: Long, page: Int = 1): Single<Pagination<Trade>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getTradesByTrader(token, traderId, page)
                        .flatMap { respPag ->
                            val trades = respPag.results.map { apiTrade -> mapToTrade(apiTrade) }
                            Single.just(mapToPagination(respPag, trades))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTradeById(token: String, tradeId: Long): Single<Trade> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getTradeById(token, tradeId)
                        .flatMap { response ->
                            Single.just(mapToTrade(response))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun postDeviceToken(token: String, deviceToken: String): Single<RequestDevice> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val deviceBody = RequestDevice(deviceToken)
                    api
                        .postDeviceToken(token, deviceBody)
                        .flatMap {
                            Single.just(it)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun myDevices(token: String): Single<List<RequestDevice>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .myDevices(token)
                        .flatMap { deviceBody ->
                            Single.just(deviceBody)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun auth(nickname: String, password: String): Single<String> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val authBody = RequestAuth(password, nickname)
                    api
                        .auth(authBody)
                        .flatMap { response ->
                            Single.just(response.auth_token)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getProfile(token: String): Single<ResponseUserProfile> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api.getUserProfile(token)
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun observeToTrader(token: String, traderId: String): Single<RequestSubscription> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val sub = RequestSubscription(traderId, null)
                    api.observeToTrader(token, sub)
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun deleteObservation(token: String, id: String): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.deleteObservation(token, id)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun subscribeToTrader(token: String, traderId: String): Single<RequestSubscription> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val sub = RequestSubscription(traderId, null)
                    api.subscribeToTrader(token, sub)
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun mySubscriptions(token: String): Single<List<Subscription>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .mySubscriptions(token)
                        .flatMap { respSubscriptions ->
                            val subscriptions = respSubscriptions.map { sub ->
                                mapToSubscription(sub)
                            }
                            Single.just(subscriptions)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun subscriptionTrades(token: String, page: Int = 1): Single<Pagination<Trade>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .subscriptionTrades(token, page)
                        .flatMap { respPagination ->
                            val trades = respPagination.results.map { respTrade ->
                                mapToTrade(respTrade)
                            }
                            Single.just(mapToPagination(respPagination, trades))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getMyTrades(token: String, page: Int = 1): Single<Pagination<Trade>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getMyTrades(token, page)
                        .flatMap {
                            val trades = it.results.map { responseTrade ->
                                mapToTrade(responseTrade)
                            }
                            Single.just(mapToPagination(it, trades))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getAllPosts(token: String, page: Int = 1): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getAllPosts(token, page)
                        .flatMap { respPag ->
                            val posts = respPag.results.map {
                                mapToPost(it)!!
                            }
                            Single.just(mapToPagination(respPag, posts))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }.subscribeOn(Schedulers.io())

    fun getPublisherPosts(token: String, page: Int = 1): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getPublisherPosts(token, page)
                        .flatMap { respPag ->
                            val posts = respPag.results.map {
                                mapToPost(it)!!
                            }
                            Single.just(mapToPagination(respPag, posts))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTraderPosts(
        token: String,
        traderId: String,
        page: Int = 1,
    ): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getTraderPosts(token, traderId, page)
                        .flatMap { respPag ->
                            val posts = respPag.results.map {
                                mapToPost(it)!!
                            }
                            Single.just(mapToPagination(respPag, posts))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTraderTradesAggregate(
        token: String,
        traderId: String,
        page: Int = 1,
    ): Single<Pagination<TradesByCompanyAggregated>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getAggregatedTrades(token, traderId, page)
                        .flatMap { respPag ->
                            val trades = respPag.results.map {
                                mapToAggregatedTrade(it)!!
                            }
                            Single.just(mapToPagination(respPag, trades))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTraderOperationsCount(
        uuidTrader: String,
    ): Single<Int> = networkStatus
        .isOnlineSingle()
        .flatMap { isOnline ->
            if (isOnline) {
                api.getTraderOperationsCount(uuidTrader)
                    .flatMap { countResponse ->
                        Single.just(countResponse.numberOfOperations)
                    }
            } else {
                Single.error(NoInternetException())
            }
        }
        .subscribeOn(Schedulers.io())


    fun getTraderTradesByCompany(
        token: String,
        traderId: String,
        companyId: Int,
        page: Int = 1,
    ): Single<Pagination<TradesSortedByCompany>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getDealsByCompany(token, traderId, companyId, page)
                        .flatMap { respPag ->
                            val trades = respPag.results.map {
                                mapToTradeByCompany(it)
                            }
                            Single.just(mapToPagination(respPag, trades))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getArgumentByTrade(
        token: String,
        tradeId: String,
    ): Single<Pagination<Argument>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getArgumentByTrade(token, tradeId)
                        .flatMap { respPag ->
                            val arguments = respPag.results.map {
                                mapToArgument(it)!!
                            }
                            Single.just(mapToPagination(respPag, arguments))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getTraderTradesJournalByCompany(
        token: String,
        companyId: Int,
        page: Int = 1,
    ): Single<Pagination<JournalTradesSortedByCompany>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    //todo(profit_count приходит не верный)
                    api
                        .getDealsJournalByCompany(token, companyId, page)
                        .flatMap { respPag ->
                            val trades = respPag.results.map {
                                mapToTradeJournalByCompany(it)
                            }
                            Single.just(mapToPagination(respPag, trades))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun signUp(
        username: String,
        password: String,
        email: String,
        phone: String,
    ): Single<ResponseSignUp> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val requestBody =
                        RequestSignUp(username, password, email, phone)
                    api.signUp(requestBody)
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun signUpAsTrader(
        username: String,
        password: String,
        email: String,
        phone: String,
        firstName: String,
        lastName: String,
        patronymic: String,
        date_of_birth: String,
        gender: String,
    ): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    val requestBody =
                        RequestSignUpAsTrader(
                            username,
                            password,
                            email,
                            phone,
                            firstName,
                            lastName,
                            patronymic,
                            date_of_birth,
                            gender
                        )
                    api.signUpAsTrader(requestBody)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun createPost(
        token: String,
        id: String,
        text: String,
        images: MutableList<ByteArray>,
    ): Single<Post> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val body = MultipartBody.Builder().apply {
                        setType(MultipartBody.FORM)
                        addFormDataPart("trader_id", id)
                        addFormDataPart("text", text)
                        addFormDataPart("pinned", "false")
                        images.forEachIndexed { index, byteArray ->
                            addPart(byteArray.mapToMultipartBodyPart(index))
                        }
                    }.build()

                    api
                        .createPost(token, body)
                        .flatMap { response ->
                            Single.just(mapToPost(response)!!)
                        }
                } else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun createArgument(
        token: String,
        id: String,
        text: String,
        trade: String,
        images: MutableList<ByteArray>,
        stopLoss: Float?,
        takeProfit: Float?,
        dealTerm: Int?,
    ): Single<Post> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val body = MultipartBody.Builder().apply {
                        setType(MultipartBody.FORM)
                        addFormDataPart("trader_id", id)
                        addFormDataPart("text", text)
                        addFormDataPart("trade", trade)
                        addFormDataPart("pinned", "false")
                        stopLoss?.let {
                            addFormDataPart("stop_loss", it.toString())
                        }
                        takeProfit?.let {
                            addFormDataPart("take_profit", it.toString())
                        }
                        dealTerm?.let {
                            addFormDataPart("deal_term", it.toString())
                        }
                        images.forEachIndexed { index, byteArray ->
                            addPart(byteArray.mapToMultipartBodyPart(index))
                        }
                    }.build()

                    api
                        .createPost(token, body)
                        .flatMap { response ->
                            Single.just(mapToPost(response)!!)
                        }
                } else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun changeAvatar(token: String, body: MultipartBody.Part): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.changeAvatar(token, body)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun deleteAvatar(token: String): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.deleteAvatar(token)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun resetPassword(email: String): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    val request = RequestResetPass(email)
                    api.resetPassword(request)
                } else Completable.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun updatePinnedPost(token: String, id: String, text: String): Single<Post> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    val requestCreatePost = RequestCreatePost(id, text, pinned = true)
                    api
                        .updatePinnedPost(token, requestCreatePost)
                        .flatMap { response ->
                            Single.just(mapToPost(response)!!)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun updatePinnedPostPatch(token: String, traderId: String, text: String): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.updatePinnedPostPatch(token, traderId, text)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun updatePublication(
        token: String,
        postId: String,
        traderId: String,
        text: String,
        imagesOnServerToDelete: MutableSet<String>,
        imagesToAdd: MutableList<ByteArray>,
    ): Single<Post> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    imagesOnServerToDelete.forEach { urlFilename ->
                        val fileName = urlFilename.substringAfterLast('/')
                        api.deleteImageInPost(token, postId, fileName)
                            .blockingSubscribe({}, {
                                it.printStackTrace()
                            })
                    }
                    api.updatePublication(
                        token, postId,
                        MultipartBody.Part.createFormData("trader_id", traderId),
                        MultipartBody.Part.createFormData("text", text),
                        imagesToAdd.mapIndexed { index, bytes -> bytes.mapToMultipartBodyPart(index) }
                    ).flatMap { response ->
                        Single.just(mapToPost(response)!!)
                    }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun updateArgument(
        token: String,
        postId: String,
        traderId: String,
        text: String,
        imagesOnServerToDelete: MutableSet<String>,
        imagesToAdd: MutableList<ByteArray>,
        stopLoss: Float?,
        takeProfit: Float?,
        dealTerm: Int?,
    ): Single<Argument> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    imagesOnServerToDelete.forEach { urlFilename ->
                        val fileName = urlFilename.substringAfterLast('/')
                        api.deleteImageInPost(token, postId, fileName)
                            .blockingSubscribe({}, {
                                it.printStackTrace()
                            })
                    }
                    api.updateArgument(
                        token, postId,
                        MultipartBody.Part.createFormData("trader_id", traderId),
                        MultipartBody.Part.createFormData("text", text),
                        imagesToAdd.mapIndexed { index, bytes -> bytes.mapToMultipartBodyPart(index) },
                        if (stopLoss != null) MultipartBody.Part.createFormData("stop_loss", stopLoss.toString()) else null,
                        if (takeProfit != null) MultipartBody.Part.createFormData("take_profit", takeProfit.toString()) else null,
                        if (dealTerm!= null) MultipartBody.Part.createFormData("deal_term", dealTerm.toString()) else null
                    ).flatMap { response ->
                        Single.just(mapToArgument(response)!!)
                    }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun deletePinnedPost(token: String): Single<Post> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .deletePinnedPost(token)
                        .flatMap { response ->
                            Single.just(mapToPost(response)!!)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun readPinnedPost(token: String): Single<Post> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .readPinnedPost(token)
                        .flatMap { response ->
                            Single.just(mapToPost(response)!!)
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun likePost(token: String, postId: Int): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline)
                    api
                        .likePost(token, postId)
                        .flatMapCompletable {
                            Completable.complete()
                        }
                else
                    Completable.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun dislikePost(token: String, postId: Int): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api
                        .dislikePost(token, postId)
                        .flatMapCompletable {
                            Completable.complete()
                        }
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun deletePost(token: String, postId: Int): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.deletePost(token, postId)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getMyPosts(
        token: String,
        page: Int = 1,
        flashedPostsOnly: Boolean = false,
    ): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getMyPosts(token, page, flashedPostsOnly)
                        .flatMap { respPag ->
                            val posts = respPag.results.mapNotNull { mapToPost(it) }
                            Single.just(mapToPagination(respPag, posts))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun logout(token: String): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.logout(token)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun sendQuestion(token: String, question: String): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    val requestQuestion = RequestQuestion(question)
                    api.sendQuestion(token, requestQuestion)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun updateTraderRegistrationInfo(
        token: String,
        traderId: String,
        requestTraderInfo: RequestTraderRegistrationInfo,
    ): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.updateTraderRegistration(token, traderId, requestTraderInfo)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun checkUsername(
        username: String,
        email: String,
    ): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
//                    val request = RequestCheckSignUpData(username = username, email = email)
                    api.checkUsername(username, email)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getPostsForGeneralNews(
        token: String?,
        page: Int = 1,
    ): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnLine ->
                if (isOnLine) {
                    api
                        .getAllPosts(token, page, flashedPostsOnly = true)
                        .flatMap { respPage ->
                            val posts = respPage.results.mapNotNull {
                                mapToPost(it)
                            }
                            Single.just(mapToPagination(respPage, posts))
                        }

                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getPostsFollowerAndObserving(
        token: String,
        page: Int = 1,
        flashedPostsOnly: Boolean = false,
    ): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getPostsFollowerAndObserving(token, page, flashedPostsOnly)
                        .flatMap { respPag ->
                            val posts = respPag.results.map {
                                mapToPost(it)!!
                            }
                            Single.just(mapToPagination(respPag, posts))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun addPostComment(
        token: String,
        postId: Int,
        text: String,
        parentCommentId: Long?,
    ): Single<Comment> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .addPostComment(token, postId, text, parentCommentId)
                        .flatMap { responseComment ->
                            Single.just(mapToComment(responseComment))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun updateComment(
        token: String,
        commentId: Long,
        text: String,
    ): Single<Comment> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .updateComment(token, commentId, text)
                        .flatMap { responseComment ->
                            Single.just(mapToComment(responseComment))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun likeComment(token: String, commentId: Long): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline)
                    api
                        .likeComment(token, commentId)
                        .flatMapCompletable {
                            Completable.complete()
                        }
                else
                    Completable.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun deleteComment(token: String, commentId: Long): Single<DeleteCommentResult> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .deleteComment(token, commentId)
                        .flatMap { responseDeleteComment ->
                            Single.just(mapToDeleteCommentResult(responseDeleteComment))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun incRepostCount(postId: Int): Single<IncPostResult> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .incRepostCount(postId)
                        .flatMap { responseIncRepostCount ->
                            Single.just(mapToIncPostResult(responseIncRepostCount))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())


    fun getComplaints(token: String): Single<List<Complaint>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .getComplaints(token)
                        .flatMap { responseComplaints ->
                            Single.just(mapToComplaints(responseComplaints))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun complainOnPost(token: String, postId: Int, complainId: Int): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.complaintOnPost(token, postId, complainId)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun complainOnComment(token: String, commentId: Long, complainId: Int): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.complaintOnComment(token, commentId, complainId)
                } else {
                    Completable.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun setFlashedPost(token: String, post: Post): Single<ResponseSetFlashedPost> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnLine ->
                if (isOnLine) {
                    api.setFlashedPost(token, post.id, true)
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getBlockUserInfo(token: String): Single<BlockUserInfo> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .getBlockUserInfo(token)
                        .flatMap { responseBlockUserInfo ->
                            Single.just(mapToBlockUserInfo(responseBlockUserInfo))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun getCommentBlockedUsers(token: String, postId: Int): Single<List<CommentBlockedUser>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .getCommentBlockedUsers(token, postId)
                        .flatMap { responseCommentBlockedUsers ->
                            Single.just(mapToCommentBlockUserInfo(responseCommentBlockedUsers))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun blockUserComments(token: String, userId: String): Single<ResultBlockUserComment> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .blockUserComments(token, RequestBlockUserComments(userId))
                        .flatMap { responseBlockCommentUser ->
                            Single.just(mapToResultBlockUserComment(responseBlockCommentUser))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())


    fun unblockUserComments(token: String, userId: String): Single<ResultUnblockUserComment> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline)
                    api
                        .unblockUserComments(token, userId)
                        .flatMap { resultUnblockUserComment ->
                            Single.just(mapToResultUnblockUserComment(resultUnblockUserComment))
                        }
                else
                    Single.error(NoInternetException())
            }
            .subscribeOn(Schedulers.io())

    fun addToBlacklist(token: String, traderId: String): Single<ResultAddToBlacklist> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api.addToBlacklist(token, traderId)
                        .flatMap { resultAddToBlacklist ->
                            Single.just(mapToResultAddToBlacklist(resultAddToBlacklist))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun deleteFromBlacklist(token: String, traderId: String): Single<ResponseAddToBlacklist> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api.deleteFromBlacklist(token, traderId)
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())

    fun getBlacklist(
        token: String,
        page: Int = 1,
    ): Single<Pagination<BlacklistItem>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getBlacklist(token, page)
                        .flatMap { respPag ->
                            val blacklist = respPag.results.map { blacklistItem ->
                                mapToBlacklistItem(blacklistItem)
                            }
                            Single.just(mapToPagination(respPag, blacklist))
                        }
                } else {
                    Single.error(NoInternetException())
                }
            }
            .subscribeOn(Schedulers.io())
}