package ru.wintrade.mvp.presenter.traders

import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
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
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}