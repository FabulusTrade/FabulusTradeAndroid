package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.TradersAllItemView

interface ITradersAllListPresenter {
    fun getCount(): Int
    fun bind(view: TradersAllItemView)
    fun openTraderStat(pos: Int)
    fun observeBtnClicked(pos: Int, isChecked: Boolean)
    fun checkIfTraderIsMe(pos: Int): Boolean
    fun isLogged(): Boolean
}