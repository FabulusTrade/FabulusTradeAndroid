package ru.fabulus.fabulustrade.mvp.presenter.registration.subscriber

import com.github.terrakok.cicerone.Router
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import retrofit2.HttpException
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.SignUpData
import ru.fabulus.fabulustrade.mvp.model.entity.exception.NoInternetException
import ru.fabulus.fabulustrade.mvp.model.entity.exception.SignUpException
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.registration.subscriber.SignUpView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.EmailValidation
import ru.fabulus.fabulustrade.util.NicknameValidation
import ru.fabulus.fabulustrade.util.PasswordValidation
import ru.fabulus.fabulustrade.util.PhoneValidation
import ru.fabulus.fabulustrade.util.isValidEmail
import ru.fabulus.fabulustrade.util.isValidNickname
import ru.fabulus.fabulustrade.util.isValidPassword
import javax.inject.Inject

class SignUpPresenter(private val asTraderRegistration: Boolean) : MvpPresenter<SignUpView>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo
    lateinit var signUpData: SignUpData

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
            viewState.showDialog(
                resourceProvider.getStringResource(R.string.error_signUp),
                resourceProvider.getStringResource(R.string.regulations_accept)
            )
            return
        }

        val formattedPhone = "8$phone"

        if (nicknameValidation == NicknameValidation.OK &&
            passwordValidation == PasswordValidation.OK &&
            isValidConfirmPassword &&
            emailValidation == EmailValidation.OK &&
            phoneValidation == PhoneValidation.OK
        ) {
            if (asTraderRegistration) {
                signUpData = SignUpData(
                    username = nickname,
                    password = password,
                    email = email,
                    phone = formattedPhone,
                    is_trader = asTraderRegistration
                )
                apiRepo
                    .checkUsername(nickname, email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        router.navigateTo(Screens.registrationAsTraderFirstScreen(signUpData))
                    }, { exception ->
                        exceptionProcessor(exception)
                    })
            } else {
                apiRepo
                    .signUp(nickname, password, email, formattedPhone)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.showDialog(
                            resourceProvider.getStringResource(R.string.is_success_reg),
                            resourceProvider.getStringResource(R.string.is_success_registration)
                        )
                        router.navigateTo(Screens.signInScreen(false))

                    }, { exception ->
                        exceptionProcessor(exception)
                    })
            }
        }
    }

    private fun exceptionProcessor(exception: Throwable) {
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
            signUpException.error_msg?.let { msg ->
                viewState.showDialog(
                    resourceProvider.getStringResource(R.string.error_signUp),
                    msg.first()
                )
            }
        }
        if (exception is NoInternetException) {
            //нет интернета
        }
    }

    fun openPrivacy() {
        router.navigateTo(Screens.webViewFragment(resourceProvider.getStringResource(R.string.privacy_url)))
    }

    fun openRules() {
        router.navigateTo(Screens.webViewFragment(resourceProvider.getStringResource(R.string.agreement_url)))
    }
}