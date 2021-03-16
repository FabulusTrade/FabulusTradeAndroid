package ru.wintrade.mvp.model.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.wintrade.mvp.model.Traders
import ru.wintrade.mvp.model.datasource.DataSource

class RetrofitImpl : DataSource<List<Traders>> {
    companion object {
        const val TRADER_LIST_URL = "http://185.46.17.66:8000/api/v1/trader/list/"
    }

    override fun getDataFromDataSource(): Observable<List<Traders>> {
        return getService().getTradersInfo()
    }

    private fun getService(): ApiService {
        return createRetrofit().create(ApiService::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TRADER_LIST_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(createGsonFactory())
            .build()
    }

    private fun createGsonFactory(): GsonConverterFactory {
        val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        return GsonConverterFactory.create(gson)
    }
}