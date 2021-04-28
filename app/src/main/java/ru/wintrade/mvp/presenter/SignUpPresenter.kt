package ru.wintrade.mvp.presenter

import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import retrofit2.HttpException
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.api.MyError
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.SignUpView
import ru.wintrade.navigation.Screens
import javax.inject.Inject


class SignUpPresenter : MvpPresenter<SignUpView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

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
        apiRepo.signUp(nickname, password, email, phone).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showToast("yes")
                router.navigateTo(Screens.SignInScreen())
            }, {
                it as HttpException
                val resp = it.response()?.errorBody()?.string()
                val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                val msg = gson.fromJson(resp, MyError::class.java)
            })
    }
}