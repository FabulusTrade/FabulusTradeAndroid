package ru.wintrade.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.wintrade.mvp.model.api.WinTradeDataSource
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderNews
import ru.wintrade.mvp.model.entity.api.*
import ru.wintrade.mvp.model.network.NetworkStatus
import ru.wintrade.util.mapToNews
import ru.wintrade.util.mapToSubscription
import ru.wintrade.util.mapToTrade
import ru.wintrade.util.mapToTrader

class ApiRepo(val api: WinTradeDataSource, val networkStatus: NetworkStatus) {

    val newTradeSubject = PublishSubject.create<Boolean>()

    fun getAllTradersSingle(token: String): Single<List<Trader>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getAllTraders(token).flatMap { list ->
                    val traders = list.map { apiTrader -> mapToTrader(apiTrader) }
                    Single.just(traders)
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

    fun getTradesByTrader(token: String, trader: Trader): Single<List<Trade>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getTradesByTrader(token, trader.id).flatMap { response ->
                    val trades = response.map { apiTrade -> mapToTrade(apiTrade, trader) }
                    Single.just(trades)
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun getTradeById(token: String, trader: Trader, tradeId: Long): Single<Trade> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
                api.getTradeById(token, trader.id, tradeId).flatMap { response ->
                    Single.just(mapToTrade(response, trader))
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

    fun mySubscriptions(token: String): Single<List<Trader>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline)
                api.mySubscriptions(token).flatMap {
                    val sub = it.map {
                        mapToSubscription(it)
                    }
                    Single.just(sub)
                }
            else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun getAllNews(token: String): Single<List<TraderNews>> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline)
                api.getAllNews(token).flatMap {
                    val news = it.map {
                        mapToNews(it)
                    }
                    Single.just(news)
                }
            else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())

    fun signInWithGoogle(idToken: String): Single<String> =
        networkStatus.isOnlineSingle().flatMap { isOnline ->
            if (isOnline) {
//                val auth = RequestAuthGoogle(idToken)
                api.signInWithGoogle(idToken).flatMap {
                    Single.just(it.auth_token)
                }
            } else
                Single.error(RuntimeException())
        }.subscribeOn(Schedulers.io())
}