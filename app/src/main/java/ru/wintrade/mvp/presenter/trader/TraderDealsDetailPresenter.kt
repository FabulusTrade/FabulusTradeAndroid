package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Deals
import ru.wintrade.mvp.presenter.adapter.IDealsDetailListPresenter
import ru.wintrade.mvp.view.item.DealsDetailItemView
import ru.wintrade.mvp.view.trader.TraderDealsDetailView

class TraderDealsDetailPresenter : MvpPresenter<TraderDealsDetailView>() {
    val listPresenter = DealsDetailRvListPresenter()

    inner class DealsDetailRvListPresenter : IDealsDetailListPresenter {
        private val dealsList = mutableListOf<Deals>()
        override fun getCount(): Int = dealsList.size

        override fun bind(view: DealsDetailItemView) {
            val deals = dealsList[view.pos]
            view.setLastDealDate(deals.date)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}