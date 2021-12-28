package ru.fabulus.fabulustrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.SignUpData
import ru.fabulus.fabulustrade.mvp.view.registration.trader.RegAsTraderFirstView
import ru.fabulus.fabulustrade.navigation.Screens
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