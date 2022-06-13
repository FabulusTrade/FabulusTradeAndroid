package ru.fabulus.fabulustrade.mvp.presenter.entrance

import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.repo.ProfileRepo
import ru.fabulus.fabulustrade.mvp.view.entrance.SplashView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashPresenter : MvpPresenter<SplashView>() {
    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var profileRepo: ProfileRepo

    @Inject
    lateinit var apiRepo: ApiRepo

    private val loadingTime = 3L

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Single.zip(
            getTimer(),
            profileRepo.get(),
            BiFunction<Long, Profile, Profile> { _, t2 -> t2 })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                profile.token = it.token
                profile.deviceToken = it.deviceToken
                profile.user = it.user
                profile.hasVisitedTutorial = it.hasVisitedTutorial
                updateFCMToken()
                viewState.goToMain()
            }, {
                viewState.goToMain()
            })
    }

    private fun getTimer() =
        Single
            .timer(loadingTime, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())

    fun updateFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                profile.apply {
                    deviceToken = task.result
                    if (token != null && deviceToken!= null) {
                        apiRepo
                            .postDeviceToken(token!!, deviceToken!!)
                            .subscribe({
                                // данные не обрабатывются
                            }, {
                                it.printStackTrace()
                            })
                    }
                }
            }
        }
    }
}