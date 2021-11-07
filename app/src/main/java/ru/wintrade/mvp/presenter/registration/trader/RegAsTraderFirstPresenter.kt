package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.SignUpData
import ru.wintrade.mvp.view.registration.trader.RegAsTraderFirstView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class RegAsTraderFirstPresenter(private val signUpData: SignUpData) : MvpPresenter<RegAsTraderFirstView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationSecondScreen() {
        router.navigateTo(Screens.registrationAsTraderSecondScreen(signUpData))
    }
}