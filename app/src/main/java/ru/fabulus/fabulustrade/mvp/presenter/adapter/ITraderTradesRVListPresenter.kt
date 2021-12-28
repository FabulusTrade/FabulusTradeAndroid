package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.TraderTradeItemView

interface ITraderTradesRVListPresenter {
    fun getCount(): Int
    fun bind(view: TraderTradeItemView)
    fun clicked(pos: Int)
}