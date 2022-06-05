package ru.fabulus.fabulustrade.mvp.presenter.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.view.trader.TraderPopularInstrumentsView
import ru.fabulus.fabulustrade.navigation.Screens
import javax.inject.Inject

class TraderPopularInstrumentsPresenter : MvpPresenter<TraderPopularInstrumentsView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
        }
    }

    fun openSignInScreen() {
        router.navigateTo(Screens.signInScreen(false))
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.signUpScreen(false))
    }
}