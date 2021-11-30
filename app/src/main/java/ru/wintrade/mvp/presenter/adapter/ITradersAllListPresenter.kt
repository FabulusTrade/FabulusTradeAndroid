package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.TradersAllItemView

interface ITradersAllListPresenter {
    fun getCount(): Int
    fun bind(view: TradersAllItemView)
    fun openTraderStat(pos: Int)
    fun observeBtnClicked(pos: Int, isChecked: Boolean)
    fun checkIfTraderIsMe(pos: Int): Boolean
}