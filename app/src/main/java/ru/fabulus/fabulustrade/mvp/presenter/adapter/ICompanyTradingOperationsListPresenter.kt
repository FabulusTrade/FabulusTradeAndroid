package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.CompanyTradingOperationsItemView

interface ICompanyTradingOperationsListPresenter {
    fun getCount(): Int
    fun bind(view: CompanyTradingOperationsItemView)
    fun itemClicked(view: CompanyTradingOperationsItemView)
}