package ru.wintrade.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.wintrade.mvp.model.api.WinTradeDataSource
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.model.entity.Subscription
import ru.wintrade.mvp.model.entity.api.*
import ru.wintrade.mvp.model.entity.common.Pagination
import ru.wintrade.mvp.model.network.NetworkStatus
import ru.wintrade.util.*

class ApiRepo(val api: WinTradeDataSource, val networkStatus: NetworkStatus) {

    val newTradeSubject = PublishSubject.create<Boolean>()

    fun getAllTraders(token: String, page: Int = 1): Single<Pagination<Trader>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getAllTraders(token, page).flatMap { respPag ->
                    val traders = respPag.results.map { apiTrader -> mapToTrader(apiTrader) }
                    Single.just(mapToPagination(respPag, traders))
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun getTraderById(token: String, traderId: Long): Single<Trader> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getTraderById(token, traderId).flatMap { response ->
                    val trader = mapToTrader(response)
                    Single.just(trader)
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun getTradesByTrader(token: String, traderId: Long, page: Int = 1): Single<Pagination<Trade>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getTradesByTrader(token, traderId, page).flatMap { respPag ->
                    val trades = respPag.results.map { apiTrade -> mapToTrade(apiTrade) }
                    Single.just(mapToPagination(respPag, trades))
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun getTradeById(token: String, traderId: Long, tradeId: Long): Single<Trade> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getTradeById(token, traderId, tradeId).flatMap { response ->
                    Single.just(mapToTrade(response))
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun postDeviceToken(token: String, deviceToken: String): Single<RequestDevice> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                val deviceBody = RequestDevice(deviceToken)
                api.postDeviceToken(token, deviceBody).flatMap {
                    Single.just(it)
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun myDevices(token: String): Single<List<RequestDevice>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.myDevices(token).flatMap { deviceBody ->
                    Single.just(deviceBody)
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun auth(nickname: String, password: String): Single<String> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                val authBody = RequestAuth(password, nickname)
                api.auth(authBody).flatMap { response ->
                    Single.just(response.auth_token)
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun getProfile(token: String): Single<ResponseProfile> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getProfile(token)
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun subscribeToTrader(token: String, traderId: Long): Single<RequestSubscription> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                val sub = RequestSubscription(traderId, null)
                api.subscribeToTrader(token, sub)
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun mySubscriptions(token: String): Single<List<Subscription>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline)
                api.mySubscriptions(token).flatMap { respSubscriptions ->
                    val subscriptions = respSubscriptions.map { sub ->
                        mapToSubscription(sub)
                    }
                    Single.just(subscriptions)
                }
            else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun subscriptionTrades(token: String, page: Int = 1): Single<Pagination<Trade>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline)
                api.subscriptionTrades(token, page).flatMap { respPagination ->
                    val trades = respPagination.results.map { respTrade ->
                        mapToTrade(respTrade)
                    }
                    Single.just(mapToPagination(respPagination, trades))
                }
            else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun getAllPosts(token: String, page: Int = 1): Single<Pagination<Post>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline)
                api.getAllPosts(token, page).flatMap { respPag ->
                    val posts = respPag.results.map {
                        mapToPost(it)!!
                    }
                    Single.just(mapToPagination(respPag, posts))
                }
            else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun signUp(
        username: String,
        password: String,
        email: String,
        phone: String
    ): Single<ResponseSignUp> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                val requestBody =
                    RequestSignUp(username, password, email, phone)
                api.signUp(requestBody)
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())
}