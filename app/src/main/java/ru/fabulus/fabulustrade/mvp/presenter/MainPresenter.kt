package ru.fabulus.fabulustrade.mvp.presenter

import android.os.Looper
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.SignUpData
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.repo.ProfileRepo
import ru.fabulus.fabulustrade.mvp.model.repo.RoomRepo
import ru.fabulus.fabulustrade.mvp.view.MainView
import ru.fabulus.fabulustrade.navigation.Screens
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileRepo: ProfileRepo

    @Inject
    lateinit var roomRepo: RoomRepo

    @Inject
    lateinit var apiRepo: ApiRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        android.os.Handler(Looper.myLooper()!!).postDelayed({
            if (profile.hasVisitedTutorial) {
                if (profile.user != null) {
                    if (profile.user!!.isTrader)
                        router.newRootScreen(Screens.traderMeMainScreen())
                    else
                        router.newRootScreen(Screens.subscriberMainScreen())
                } else
                    router.newRootScreen(Screens.tradersMainScreen(null))
            } else
                router.newRootScreen(Screens.onBoardScreen())
        }, 1)
    }


    fun tradersMenuClicked() {
        router.replaceScreen(Screens.tradersMainScreen(null))
    }

    fun generalFeedClicked() {
        router.replaceScreen(Screens.generalFeedFragment())
    }

    fun observationMenuClicked() {
        if (profile.user != null) {
            if (profile.user!!.isTrader)
                router.replaceScreen(Screens.traderMeMainScreen())
            else
                router.replaceScreen(Screens.subscriberMainScreen())

        } else
            router.navigateTo(Screens.signInScreen(false))
    }

    fun aboutWTMenuClicked() {
        router.replaceScreen(Screens.aboutWinTradeScreen())
    }

    fun questionMenuClicked() {
        if (profile.user != null)
            router.replaceScreen(Screens.questionScreen())
        else
            router.navigateTo(Screens.signInScreen(false))
    }

    fun settingsMenuClicked() {
        router.replaceScreen(Screens.settingsScreen())
    }

    fun friendInviteMenuClicked() {
        if (profile.user != null)
            router.replaceScreen(Screens.friendInviteScreen())
        else
            router.navigateTo(Screens.signInScreen(false))
    }

    fun exitClicked() {
        profile.token?.let {
            apiRepo.logout(it).observeOn(AndroidSchedulers.mainThread()).subscribe()
        }
        profile.deviceToken = null
        profile.token = null
        profile.user = null
        profileRepo.save(profile).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                viewState.exit()
            }, {}
        )
    }

    fun onDrawerOpened() {
        profile.token?.let { token ->
            apiRepo.getProfile(token).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    viewState.setupHeader(it.avatar, it.username)
                }, {}
            )
        }
    }

    fun openSearchScreen() {
        if (profile.user == null) {
            router.navigateTo(Screens.signInScreen(false))
        } else {
            //navigateTo SearchScreen must be here
        }
    }

    fun openShareScreen() {
        if (profile.user == null) {
            router.navigateTo(Screens.signInScreen(false))
        } else {
            //navigateTo ShareScreen must be here
        }
    }

    fun openWinScreen() {
        if (profile.user == null) {
            router.navigateTo(Screens.signInScreen(false))
        } else {
            //navigateTo WinScreen must be here
        }
    }

    fun openSignUpTraderScreen() {
        router.navigateTo(Screens.registrationAsTraderFirstScreen(signUpData = SignUpData(is_trader = true)))
    }

    fun backClicked() {
        router.exit()
    }
}