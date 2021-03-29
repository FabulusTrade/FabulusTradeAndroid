package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.SubscriberTradeItemView

interface ISubscriberTradesRVListPresenter {
    fun getCount(): Int
    fun bind(view: SubscriberTradeItemView)
    fun clicked(pos: Int)
}