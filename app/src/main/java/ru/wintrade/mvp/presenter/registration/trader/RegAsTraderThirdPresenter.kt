package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.view.registration.trader.RegAsTraderThirdView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class RegAsTraderThirdPresenter : MvpPresenter<RegAsTraderThirdView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationSecondScreen(traderInfo: TraderRegistrationInfo) {
        router.navigateTo(Screens.registrationAsTraderSecondScreen(traderInfo))
    }
}