package ru.wintrade.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.wintrade.mvp.model.api.WinTradeDataSource
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.network.NetworkStatus

class ApiRepo(val api: WinTradeDataSource, val networkStatus: NetworkStatus) {

    fun getAllTradersSingle(): Single<List<Trader>> = networkStatus.isOnlineSingle().flatMap { isOnline ->
        if (isOnline) {
            api.getAllTraders().flatMap { list ->
                val traders = list.map { apiTrader -> Trader(apiTrader.fio, apiTrader.followersCount) }
                Single.just(traders)
            }
        } else
            Single.error(RuntimeException())
    }.subscribeOn(Schedulers.io())

}