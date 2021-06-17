package ru.wintrade.mvp.view.entrance

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.util.EmailValidation

@StateStrategyType(AddToEndStrategy::class)
interface ResetPasswordView : MvpView {
    fun init()
    fun setEmailError(validation: EmailValidation)
    fun showSuccessDialog()
    fun showAlertDialog()
}