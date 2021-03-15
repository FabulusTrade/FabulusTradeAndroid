package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.Traders
import ru.wintrade.mvp.presenter.adapter.IAllTradersListPresenter
import ru.wintrade.mvp.view.AllTradersView
import ru.wintrade.mvp.view.item.AllTradersItemView
import javax.inject.Inject

class AllTradersPresenter : MvpPresenter<AllTradersView>() {
    @Inject
    lateinit var router: Router

    val listPresenter = AllTradersListPresenter()

    inner class AllTradersListPresenter : IAllTradersListPresenter {
        var traderList = mutableListOf<Traders>()
        override fun getCount(): Int = traderList.size

        override fun bind(view: AllTradersItemView) {
            view.setTraderFio(traderList[view.pos].fio)
            view.setTraderFollowerCount(traderList[view.pos].fCount)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}