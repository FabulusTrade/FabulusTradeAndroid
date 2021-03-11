package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.SignUpView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class SignUpPresenter: MvpPresenter<SignUpView>() {

    @Inject
    lateinit var router: Router

    private var privacyState = false
    private var rulesState = false
    private var nickname = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""
    private var phone = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun privacyCheckChanged(checked: Boolean) {
        privacyState = checked
    }

    fun rulesCheckChanged(checked: Boolean) {
        rulesState = checked
    }

    fun nicknameChanged(nickname: String) {
        this.nickname = nickname
    }

    fun emailChanged(email: String) {
        this.email = email
    }

    fun passwordChanged(password: String) {
        this.password = password
    }

    fun confirmPasswordChanged(confirmPassword: String) {
        this.confirmPassword = confirmPassword
    }

    fun phoneChanged(phone: String) {
        this.phone = phone
    }

    fun createProfileClicked() {
        router.navigateTo(Screens.SmsConfirmScreen(phone))
    }
}