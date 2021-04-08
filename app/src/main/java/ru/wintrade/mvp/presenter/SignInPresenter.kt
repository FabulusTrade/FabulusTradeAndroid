package ru.wintrade.mvp.presenter

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.repo.RoomRepo
import ru.wintrade.mvp.view.SignInView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.mapToProfile
import javax.inject.Inject

class SignInPresenter : MvpPresenter<SignInView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var roomRepo: RoomRepo

    @Inject
    lateinit var profileStorage: ProfileStorage

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
                val deviceToken = task.result
                apiRepo.auth(nickname, password).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { authToken ->
                        val token = "Token $authToken"
                        apiRepo.postDeviceToken(token, task.result!!).subscribe(
                            {},
                            {
                                it.printStackTrace()
                            }
                        )


                        apiRepo.getProfile(token).subscribe(
                            { response ->
                                val profile = mapToProfile(response, token, deviceToken!!)
                                profileStorage.profile = profile
                                roomRepo.insertProfile(profile).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                    {
                                        router.newRootScreen(Screens.SubscriberMainScreen())
                                    },
                                    {
                                        it.printStackTrace()
                                    }
                                )
                            },
                            {
                                it.printStackTrace()
                            }
                        )

                    },
                    {
                        viewState.showToast("Неверные данные")
                    }
                )
            }
        }
    }


}