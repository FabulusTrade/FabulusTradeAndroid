package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.LoadingItemView

interface ILoadingListPresenter {
    fun getCount(): Int
    fun bindView(view: LoadingItemView)
}