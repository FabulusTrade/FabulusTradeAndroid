package ru.fabulus.fabulustrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.view.registration.trader.RegAsTraderFourView
import javax.inject.Inject

class RegAsTraderFourPresenter : MvpPresenter<RegAsTraderFourView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}