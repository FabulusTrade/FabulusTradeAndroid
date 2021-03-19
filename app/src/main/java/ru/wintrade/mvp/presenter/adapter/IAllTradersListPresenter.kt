package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.AllTradersItemView

interface IAllTradersListPresenter {
    fun getCount(): Int
    fun bind(view: AllTradersItemView)
    fun openTraderStat(pos: Int)
}