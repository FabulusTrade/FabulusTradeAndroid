package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.CompanyTradingOperationsJournalItemView

interface ICompanyTradingOperationsJournalListPresenter {
    fun getCount(): Int
    fun bind(view: CompanyTradingOperationsJournalItemView)
    fun itemClicked(view: CompanyTradingOperationsJournalItemView)
}