package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.SubscriberTradeItemView

interface ISubscriberTradesRVListPresenter {
    fun getCount(): Int
    fun bind(view: SubscriberTradeItemView)
    fun clicked(pos: Int)
}