package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Deals
import ru.wintrade.mvp.presenter.adapter.ICompanyTradingOperationsListPresenter
import ru.wintrade.mvp.view.item.CompanyTradingOperationsItemView
import ru.wintrade.mvp.view.trader.CompanyTradingOperationsView

class CompanyTradingOperationsPresenter : MvpPresenter<CompanyTradingOperationsView>() {
    val listPresenter = CompanyTradingOperationsRvListPresenter()

    inner class CompanyTradingOperationsRvListPresenter : ICompanyTradingOperationsListPresenter {
        private val dealsList = mutableListOf<Deals>()
        override fun getCount(): Int = dealsList.size

        override fun bind(view: CompanyTradingOperationsItemView) {
            val deals = dealsList[view.pos]
            view.setLastOperationDate(deals.date)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}