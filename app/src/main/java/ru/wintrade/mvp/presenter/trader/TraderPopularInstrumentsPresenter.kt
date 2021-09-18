package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.view.trader.TraderPopularInstrumentsView
import ru.wintrade.navigation.Screens
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
        router.navigateTo(Screens.signInScreen())
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.signUpScreen())
    }
}