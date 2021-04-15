package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.TraderNewsItemView

interface ITraderNewsRVListPresenter {
    fun getCount(): Int
    fun bind(view: TraderNewsItemView)
}