package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Gender
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.SignUpData
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.repo.ProfileRepo
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.view.registration.trader.RegAsTraderThirdView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class RegAsTraderThirdPresenter(
    private val signUpData: SignUpData
) : MvpPresenter<RegAsTraderThirdView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var profileRepo: ProfileRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        checkUserTraderOrFollower()
        viewState.init()
    }

    private fun checkUserTraderOrFollower() {
        profile.user?.let {
            viewState.renderInstructionText(resourceProvider.getStringResource(R.string.trader_reg_3_fromFollowerToTrader))
        }
            ?: viewState.renderInstructionText(resourceProvider.getStringResource(R.string.trader_reg_3_becomeToTrader))
    }

    fun openRegistrationSecondScreen() {
        router.backTo(Screens.registrationAsTraderSecondScreen(signUpData))
    }

    fun openNextStageScreen() {
        profile.token?.let {
            profileRepo.userProfileRemoteDataSource
                .get(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newProfile ->
                    profile.user = newProfile

                    profileRepo
                        .save(profile)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            router.newRootChain(Screens.traderMeMainScreen())
                        }, {
                            // Ошибка не обрабатывается
                        })
                }, {
                    // Ошибка не обрабатывается
                })
        } ?: router.newRootChain(Screens.signInScreen(false))
    }

    fun saveTraderRegistrationInfo() {
        if (profile.user == null) {
            apiRepo
                .signUpAsTrader(
                    signUpData.username!!,
                    signUpData.password!!,
                    signUpData.email!!,
                    signUpData.phone!!,
                    signUpData.first_name!!,
                    signUpData.last_name!!,
                    signUpData.patronymic!!,
                    signUpData.date_of_birth!!,
                    signUpData.gender!!
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showSuccessfulPatchData()
                }, {
                    viewState.showErrorPatchData(it)
                })
        } else {
            profile.token?.let { token ->
                val traderRegistrationInfo = TraderRegistrationInfo(
                    signUpData.date_of_birth,
                    signUpData.first_name,
                    signUpData.last_name,
                    signUpData.patronymic,
                    Gender.getGender(signUpData.gender!!)
                )
                profile.user?.let { userProfile ->
                    apiRepo
                        .updateTraderRegistrationInfo(
                            token,
                            userProfile.id,
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
    }
}