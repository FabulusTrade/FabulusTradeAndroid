package ru.wintrade.mvp.presenter.traders

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.SignUpData
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.traders.TradersMainView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TradersMainPresenter(val checkedFilter: Int) : MvpPresenter<TradersMainView>() {
    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init(checkedFilter)
        profile.user?.let {
            if (it.isTrader) {
                viewState.setRegistrationBtnVisible(isVisible = false)
            } else {
                viewState.setRegistrationBtnVisible(isVisible = true)
            }
        }
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }

    fun openRegistrationScreen(isTraderRegistrationButtonClicked: Boolean) {
        when {
            profile.user == null && !isTraderRegistrationButtonClicked -> {
                router.navigateTo(Screens.signInScreen(false))
            }
            profile.user == null && isTraderRegistrationButtonClicked -> {
                router.navigateTo(Screens.signInScreen(true))
            }
            else -> {
                val signUpData = SignUpData(is_trader = true)
                router.navigateTo(Screens.registrationAsTraderFirstScreen(signUpData))
            }
        }
    }
}