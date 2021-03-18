package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.SignInView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class SignInPresenter : MvpPresenter<SignInView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationScreen() {
        //router.navigateTo(Screens.SignUpScreen())
    }

    fun loginBtnClicked() {
        router.newRootScreen(Screens.AllTradersScreen())
    }
}