package ru.wintrade.mvp.presenter

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.repo.RoomRepo
import ru.wintrade.mvp.view.SignInView
import ru.wintrade.navigation.Screens
import ru.wintrade.ui.App
import javax.inject.Inject

class SignInPresenter : MvpPresenter<SignInView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var roomRepo: RoomRepo

    @Inject
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationScreen() {
        //router.navigateTo(Screens.SignUpScreen())
    }

    fun loginBtnClicked(nickname: String, password: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                profile.deviceToken = task.result
                apiRepo.auth(nickname, password).subscribe(
                    { authToken ->

                        profile.token = "Token $authToken"

                        apiRepo.myDevices(profile.token!!).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                { list ->
                                    var isTokenUploaded = false
                                    list.forEach { deviceBody ->
                                        if (profile.deviceToken == deviceBody.registration_id)
                                            isTokenUploaded = true
                                    }
                                    if (!isTokenUploaded) {
                                        postDeviceToken()
                                    }
                                    roomRepo.insertProfile(profile).subscribe()
                                    router.newRootScreen(Screens.SubscriberMainScreen())
                                },
                                {

                                }
                            )
                    },
                    {
                    }
                )
            }
        }
    }

    private fun postDeviceToken() =
        apiRepo.postDeviceToken(profile.token!!, profile.deviceToken!!).subscribe(
            {
                Log.e("ir", "dsa")
            },
            {
                Log.e("dasd", it.message.toString())
            }
        )
}