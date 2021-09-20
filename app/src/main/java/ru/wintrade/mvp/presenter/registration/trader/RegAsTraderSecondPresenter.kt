package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.view.registration.trader.RegAsTraderSecondView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class RegAsTraderSecondPresenter : MvpPresenter<RegAsTraderSecondView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationFirstScreen() {
        router.navigateTo(Screens.registrationAsTraderFirstScreen())
    }

    fun openRegistrationThirdScreen(traderInfo: TraderRegistrationInfo) {
        router.navigateTo(Screens.registrationAsTraderThirdScreen(traderInfo))
    }

}