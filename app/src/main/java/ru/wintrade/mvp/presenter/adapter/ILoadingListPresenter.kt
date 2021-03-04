package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.LoadingItemView

interface ILoadingListPresenter {
    fun onSkipBtnClick()
    fun getCount(): Int
    fun bindView(view: LoadingItemView)
}