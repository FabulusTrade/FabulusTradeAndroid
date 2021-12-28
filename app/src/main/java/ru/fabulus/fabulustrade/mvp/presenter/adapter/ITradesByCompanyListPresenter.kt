package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.TradesByCompanyItemView

interface ITradesByCompanyListPresenter {
    fun getCount(): Int
    fun bind(view: TradesByCompanyItemView)
    fun onItemClick(view: TradesByCompanyItemView)
}