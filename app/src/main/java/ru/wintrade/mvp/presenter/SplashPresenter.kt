package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.RoomRepo
import ru.wintrade.mvp.view.SplashView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashPresenter : MvpPresenter<SplashView>() {

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var roomRepo: RoomRepo

    private val loadingTime = 3L

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Single.zip(
            getTimer(),
            roomRepo.getProfile().onErrorReturn { Profile() },
            BiFunction<Long, Profile, Profile> { _, t2 -> t2 })
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    viewState.goToMain()
                },
                {}
            )

    }

    private fun getTimer() =
        Single.timer(loadingTime, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
}