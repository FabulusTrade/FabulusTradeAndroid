package ru.wintrade.mvp.presenter.traders

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.traders.TradersMainView
import javax.inject.Inject

class TradersMainPresenter : MvpPresenter<TradersMainView>() {
    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileStorage: ProfileStorage

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}