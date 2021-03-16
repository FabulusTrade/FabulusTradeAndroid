package ru.wintrade.mvp.model.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import ru.wintrade.mvp.model.Traders

interface ApiService {
    @GET(".")
    fun getTradersInfo() : Observable<List<Traders>>
}