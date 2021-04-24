package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.RoomRepo
import ru.wintrade.mvp.view.SplashView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashPresenter : MvpPresenter<SplashView>() {

    @Inject
    lateinit var profileStorage: ProfileStorage

    @Inject
    lateinit var roomRepo: RoomRepo

    private val loadingTime = 3L

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Single.zip(
            getTimer(),
            roomRepo.getProfile().onErrorReturn {
                Profile(
                    -1,
                    "",
                    "",
                    kval = false,
                    isTrader = false,
                    firstName = "",
                    lastName = "",
                    patronymic = "",
                    token = "",
                    deviceToken = "",
                    avatar = null,
                    dateJoined = "",
                    phone = null,
                    followersCount = -1,
                    subscriptionsCount = -1
                )
            },
            BiFunction<Long, Profile, Profile> { _, t2 -> t2 })
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (it.id != -1L)
                        profileStorage.profile = it
                    viewState.goToMain()
                },
                {
                    viewState.goToMain()
                }
            )
    }

    private fun getTimer() =
        Single.timer(loadingTime, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
}