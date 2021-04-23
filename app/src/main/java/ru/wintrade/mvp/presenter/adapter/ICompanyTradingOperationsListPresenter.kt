package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.CompanyTradingOperationsItemView

interface ICompanyTradingOperationsListPresenter {
    fun getCount(): Int
    fun bind(view: CompanyTradingOperationsItemView)
}