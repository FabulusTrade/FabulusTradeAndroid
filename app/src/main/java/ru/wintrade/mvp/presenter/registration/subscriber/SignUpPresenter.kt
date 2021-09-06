package ru.wintrade.mvp.presenter.registration.subscriber

import com.github.terrakok.cicerone.Router
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import retrofit2.HttpException
import ru.wintrade.mvp.model.entity.exception.NoInternetException
import ru.wintrade.mvp.model.entity.exception.SignUpException
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.registration.subscriber.SignUpView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.*
import javax.inject.Inject

class SignUpPresenter : MvpPresenter<SignUpView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    private var password = ""
    private var isCorrectPhone = false
    private var phone = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun onNicknameInputFocusLost(nickname: String) {
        if (nickname.isNotEmpty())
            viewState.setNicknameError(isValidNickname(nickname))
    }

    fun onPasswordInputFocusLost(password: String) {
        this.password = password
        if (password.isNotEmpty())
            viewState.setPasswordError(isValidPassword(password))
    }

    fun onConfirmPasswordInputFocusLost(confirmPassword: String) {
        if (confirmPassword.isNotEmpty())
            viewState.setPasswordConfirmError(confirmPassword == password)
    }

    fun onPhoneInputFocusLost() {
        if (phone.isNotEmpty() && !isCorrectPhone)
            viewState.setPhoneError(PhoneValidation.INCORRECT)
        else
            viewState.setPhoneError(PhoneValidation.OK)
    }

    fun onEmailInputFocusLost(email: String) {
        if (email.isNotEmpty())
            viewState.setEmailError(isValidEmail(email))
    }

    fun onPhoneChanged(phone: String, isCorrect: Boolean) {
        this.phone = phone
        isCorrectPhone = isCorrect
    }

    fun createProfileClicked(
        nickname: String,
        password: String,
        confirmPassword: String,
        email: String,
        isRulesAccepted: Boolean,
        isPrivacyAccepted: Boolean
    ) {
        val nicknameValidation = isValidNickname(nickname)
        val passwordValidation = isValidPassword(password)
        val isValidConfirmPassword = password == confirmPassword
        val emailValidation = isValidEmail(email)
        val phoneValidation = if (isCorrectPhone) PhoneValidation.OK else PhoneValidation.INCORRECT

        viewState.setNicknameError(nicknameValidation)
        viewState.setPasswordError(passwordValidation)
        viewState.setPasswordConfirmError(isValidConfirmPassword)
        viewState.setEmailError(emailValidation)
        viewState.setPhoneError(phoneValidation)

        if (!(isPrivacyAccepted && isRulesAccepted)) {
            viewState.showRegulationsAcceptDialog()
            return
        }

        val formattedPhone = "8$phone"

        if (nicknameValidation == NicknameValidation.OK &&
            passwordValidation == PasswordValidation.OK &&
            isValidConfirmPassword &&
            emailValidation == EmailValidation.OK &&
            phoneValidation == PhoneValidation.OK
        ) {
            apiRepo.signUp(nickname, password, email, formattedPhone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showSuccessDialog()
                    router.navigateTo(Screens.signInScreen())
                }, { exception ->
                    if (exception is HttpException) {
                        val resp = exception.response()?.errorBody()?.string()
                        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                        val signUpException = gson.fromJson(resp, SignUpException::class.java)
                        if (signUpException.email != null)
                            viewState.setEmailError(EmailValidation.ALREADY_EXISTS)
                        if (signUpException.username != null)
                            viewState.setNicknameError(NicknameValidation.ALREADY_EXISTS)
                        if (signUpException.phone == null)
                            viewState.setPhoneError(PhoneValidation.ALREADY_EXISTS)
                    }
                    if (exception is NoInternetException) {
                        //нет интернета
                    }
                })
        }
    }
}