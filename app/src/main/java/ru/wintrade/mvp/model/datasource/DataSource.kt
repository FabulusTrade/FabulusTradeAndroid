package ru.wintrade.mvp.model.datasource

import io.reactivex.rxjava3.core.Observable

interface DataSource<T> {
    fun getDataFromDataSource(): Observable<T>
}