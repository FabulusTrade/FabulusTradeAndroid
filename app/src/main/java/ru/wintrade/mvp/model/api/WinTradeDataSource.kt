package ru.wintrade.mvp.model.api


import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ru.wintrade.mvp.model.entity.api.ApiTrader

interface WinTradeDataSource {
    @GET("/api/v1/trader/list/")
    fun getAllTraders() : Single<List<ApiTrader>>
}