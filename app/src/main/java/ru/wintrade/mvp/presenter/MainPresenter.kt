package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.repo.RoomRepo
import ru.wintrade.mvp.view.MainView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

@InjectViewState
class MainPresenter(val hasVisitedTutorial: Boolean) : MvpPresenter<MainView>() {

    @Inject
    lateinit var profileStorage: ProfileStorage

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var roomRepo: RoomRepo

    @Inject
    lateinit var apiRepo: ApiRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        profileStorage.profile?.let { profile ->
            if (profile.isTrader) {
                apiRepo.getTraderById(profile.token, profile.id)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        router.newRootScreen(Screens.TraderStatScreen(it))
                    }, {})
            } else router.newRootScreen(Screens.SubscriberMainScreen())
        } ?: hasVisited()
    }

    private fun hasVisited() {
        if (hasVisitedTutorial)
            router.newRootScreen(Screens.SignInScreen())
        else
            router.newRootScreen(Screens.OnBoardScreen())
    }

    fun openTradersScreen() {
        router.replaceScreen(Screens.TradersMainScreen())
    }

    fun openSubscriberObservationScreen() {
        router.replaceScreen(Screens.SubscriberMainScreen())
    }

    fun exitClicked() {
        roomRepo.deleteProfile().observeOn(AndroidSchedulers.mainThread()).subscribe({
            profileStorage.profile = null
            viewState.exit()
        }, {})
    }

    fun onDrawerOpened() {
        val profile = profileStorage.profile
        viewState.setupHeader(profile!!.avatar, profile.username)
    }

    fun backClicked() {
        router.exit()
    }
}