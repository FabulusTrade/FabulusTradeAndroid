package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.RegistrationTraderData
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.repo.ProfileRepo
import ru.wintrade.mvp.view.registration.trader.RegAsTraderThirdView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class RegAsTraderThirdPresenter(
    private val traderRegistrationInfo: TraderRegistrationInfo,
    private val registrationData: RegistrationTraderData
) : MvpPresenter<RegAsTraderThirdView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var profileRepo: ProfileRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationSecondScreen() {
        router.navigateTo(Screens.registrationAsTraderSecondScreen(traderRegistrationInfo))
    }

    fun openNextStageScreen() {
        if (registrationData.isFastWay) {
            router.newRootChain(Screens.signInScreen(false))
        } else {
            profile.token?.let {
                profileRepo.userProfileRemoteDataSource.get(it)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        { newProfile ->
                            profile.user = newProfile
                            profileRepo.save(profile).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    { router.newRootChain(Screens.traderMeMainScreen()) }, {})
                        }, {})
            }
        }
    }

    fun saveTraderRegistrationInfo() {
        profile.token?.let { token ->
            apiRepo.updateTraderRegistrationInfo(
                token,
                registrationData.traderId,
                traderRegistrationInfo.toRequest()
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