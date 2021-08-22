package ru.wintrade.mvp.presenter

import android.os.Looper
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.repo.ProfileRepo
import ru.wintrade.mvp.model.repo.RoomRepo
import ru.wintrade.mvp.view.MainView
import ru.wintrade.navigation.Screens
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
                        router.newRootScreen(Screens.TraderMeMainScreen())
                    else
                        router.newRootScreen(Screens.SubscriberMainScreen())
                } else
                    router.newRootScreen(Screens.TradersMainScreen())
            } else
                router.newRootScreen(Screens.OnBoardScreen())
        }, 1)
    }


    fun tradersMenuClicked() {
        router.replaceScreen(Screens.TradersMainScreen())
    }

    fun observationMenuClicked() {
        if (profile.user != null) {
            if (profile.user!!.isTrader)
                router.replaceScreen(Screens.TraderMeMainScreen())
            else
                router.replaceScreen(Screens.SubscriberMainScreen())

        } else
            router.navigateTo(Screens.SignInScreen())
    }

    fun aboutWTMenuClicked() {
        router.replaceScreen(Screens.AboutWinTradeScreen())
    }

    fun questionMenuClicked() {
        if (profile.user != null)
            router.replaceScreen(Screens.QuestionScreen())
        else
            router.navigateTo(Screens.SignInScreen())
    }

    fun settingsMenuClicked() {
        router.replaceScreen(Screens.SettingsScreen())
    }

    fun friendInviteMenuClicked() {
        if (profile.user != null)
            router.replaceScreen(Screens.FriendInviteScreen())
        else
            router.navigateTo(Screens.SignInScreen())
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
            router.navigateTo(Screens.SignInScreen())
        } else {
//navigateTo SearchScreen must be here
        }
    }

    fun openShareScreen() {
        if (profile.user == null) {
            router.navigateTo(Screens.SignInScreen())
        } else {
//navigateTo ShareScreen must be here
        }
    }

    fun openWinScreen() {
        if (profile.user == null) {
            router.navigateTo(Screens.SignInScreen())
        } else {
//navigateTo WinScreen must be here
        }
    }

    fun backClicked() {
        router.exit()
    }
}