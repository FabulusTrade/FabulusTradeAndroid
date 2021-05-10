package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.util.EmailValidation
import ru.wintrade.util.NicknameValidation
import ru.wintrade.util.PasswordValidation
import ru.wintrade.util.PhoneValidation

@StateStrategyType(AddToEndSingleStrategy::class)
interface SignUpView: MvpView {
    fun init()
    fun setNicknameError(validation: NicknameValidation)
    fun setPasswordError(validation: PasswordValidation)
    fun setPasswordConfirmError(isCorrect: Boolean)
    fun setEmailError(validation: EmailValidation)
    fun setPhoneError(validation: PhoneValidation)
    @StateStrategyType(SkipStrategy::class)
    fun showRegulationsAcceptToast()
}