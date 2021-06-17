package ru.wintrade.mvp.presenter.entrance

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.entrance.ResetPasswordView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.EmailValidation
import ru.wintrade.util.isValidEmail
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
        router.replaceScreen(Screens.SignInScreen())
    }

    fun resetPassBtnClicked(email: String) {
        val emailValidation = isValidEmail(email)
        viewState.setEmailError(emailValidation)
        if (emailValidation == EmailValidation.OK) {
            apiRepo.resetPassword(email).observeOn(AndroidSchedulers.mainThread()).subscribe({
                viewState.showSuccessDialog()
            },{
                viewState.showAlertDialog()
            })
        }
    }
}