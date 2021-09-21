package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.registration.trader.RegAsTraderThirdView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class RegAsTraderThirdPresenter : MvpPresenter<RegAsTraderThirdView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationSecondScreen(traderInfo: TraderRegistrationInfo) {
        router.navigateTo(Screens.registrationAsTraderSecondScreen(traderInfo))
    }

    fun openNextStageScreen() {
        router.navigateTo(Screens.tradersMainScreen())
    }

    fun saveTraderRegistrationInfo(traderInfo: TraderRegistrationInfo) {
        profile.token?.let { token ->
            profile.user?.let { userProfile ->
                apiRepo.updateTraderRegistrationInfo(
                    token,
                    userProfile.id,
                    traderInfo.dateOdBirth,
                    traderInfo.firstName,
                    traderInfo.lastName,
                    traderInfo.patronymic,
                    traderInfo.gender
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.showSuccessfulPatchData()
                    }, {
                        viewState.showErrorPatchData(it)
                    })
            }
        }
    }
}