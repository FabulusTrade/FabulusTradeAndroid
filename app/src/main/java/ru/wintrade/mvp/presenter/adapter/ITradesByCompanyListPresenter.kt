package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.TradesByCompanyItemView

interface ITradesByCompanyListPresenter {
    fun getCount(): Int
    fun bind(view: TradesByCompanyItemView)
    fun onItemClick(view: TradesByCompanyItemView)
}