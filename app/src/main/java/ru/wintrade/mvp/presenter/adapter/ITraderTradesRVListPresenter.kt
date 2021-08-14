package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.TraderTradeItemView

interface ITraderTradesRVListPresenter {
    fun getCount(): Int
    fun bind(view: TraderTradeItemView)
    fun clicked(pos: Int)
}