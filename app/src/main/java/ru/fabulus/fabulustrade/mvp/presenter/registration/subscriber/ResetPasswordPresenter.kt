package ru.fabulus.fabulustrade.mvp.presenter.registration.subscriber

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.view.registration.subscriber.ResetPasswordView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.EmailValidation
import ru.fabulus.fabulustrade.util.isValidEmail
import javax.inject.Inject

class ResetPasswordPresenter : MvpPresenter<ResetPasswordView>() {
    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openSignInScreen() {
        router.replaceScreen(Screens.signInScreen(false))
    }

    fun resetPassBtnClicked(email: String) {
        val emailValidation = isValidEmail(email)
        viewState.setEmailError(emailValidation)
        if (emailValidation == EmailValidation.OK) {
            apiRepo
                .resetPassword(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showSuccessDialog()
                }, {
                    viewState.showAlertDialog()
                })
        }
    }
}