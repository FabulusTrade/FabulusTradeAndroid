package ru.fabulus.fabulustrade.mvp.view.registration.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.util.EmailValidation
import ru.fabulus.fabulustrade.util.NicknameValidation
import ru.fabulus.fabulustrade.util.PasswordValidation
import ru.fabulus.fabulustrade.util.PhoneValidation

@StateStrategyType(AddToEndSingleStrategy::class)
interface SignUpView : MvpView {
    fun init()
    fun setNicknameError(validation: NicknameValidation)
    fun setPasswordError(validation: PasswordValidation)
    fun setPasswordConfirmError(isCorrect: Boolean)
    fun setEmailError(validation: EmailValidation)
    fun setPhoneError(validation: PhoneValidation)

    @StateStrategyType(SkipStrategy::class)
    fun showDialog(title: String, message: String)
}