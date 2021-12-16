package ru.wintrade.mvp.model.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.MultipartBody
import ru.wintrade.mvp.model.api.WinTradeApi
import ru.wintrade.mvp.model.entity.*
import ru.wintrade.mvp.model.entity.api.*
import ru.wintrade.mvp.model.entity.common.Pagination
import ru.wintrade.mvp.model.entity.exception.NoInternetException
import ru.wintrade.mvp.model.network.NetworkStatus
import ru.wintrade.util.*

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
        traderId: String
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

    fun getTraderById(token: String, traderId: Long): Single<Trader> =
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
        page: Int = 1
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
        page: Int = 1
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
        page: Int = 1
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

    fun signUp(
        username: String,
        password: String,
        email: String,
        phone: String
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
        gender: String
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
        images: List<MultipartBody.Part>
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
                        images.forEach { addPart(it) }
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
        text: String
    ): Completable =
        networkStatus
            .isOnlineSingle()
            .flatMapCompletable { isOnline ->
                if (isOnline) {
                    api.updatePublication(token, postId, traderId, text)
                } else {
                    Completable.error(NoInternetException())
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

    fun getMyPosts(token: String, page: Int = 1): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getMyPosts(token, page)
                        .flatMap { respPag ->
                            val posts = respPag.results.map { mapToPost(it)!! }
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
        requestTraderInfo: RequestTraderRegistrationInfo
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
        email: String
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

    fun getPostsFollowerAndObserving(
        token: String,
        page: Int = 1
    ): Single<Pagination<Post>> =
        networkStatus
            .isOnlineSingle()
            .flatMap { isOnline ->
                if (isOnline) {
                    api
                        .getPostsFollowerAndObserving(token, page)
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

}