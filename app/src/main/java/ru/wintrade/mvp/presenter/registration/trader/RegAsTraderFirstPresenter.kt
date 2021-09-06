package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.view.registration.trader.RegAsTraderFirstView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class RegAsTraderFirstPresenter : MvpPresenter<RegAsTraderFirstView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationSecondScreen() {
        router.navigateTo(Screens.registrationAsTraderSecondScreen())
    }

    fun closeRegistrationScreen() {
        router.backTo(Screens.tradersMainScreen())
    }
}