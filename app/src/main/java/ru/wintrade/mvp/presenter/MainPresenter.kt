package ru.wintrade.mvp.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.view.MainView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

@InjectViewState
class MainPresenter(val hasVisitedTutorial: Boolean) : MvpPresenter<MainView>() {

    @Inject
    lateinit var profileStorage: ProfileStorage

    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        if (profileStorage.profile != null)
            router.newRootScreen(Screens.SubscriberMainScreen())
        else {
            if (hasVisitedTutorial)
                router.newRootScreen(Screens.SignInScreen())
            else
                router.newRootScreen(Screens.OnBoardScreen())
        }

    }

    fun openTradersScreen(){
        router.replaceScreen(Screens.TradersMainScreen())
    }

    fun openSubscriberObservationScreen() {
        router.replaceScreen(Screens.SubscriberMainScreen())
    }

    fun backClicked() {
        router.exit()
    }
}