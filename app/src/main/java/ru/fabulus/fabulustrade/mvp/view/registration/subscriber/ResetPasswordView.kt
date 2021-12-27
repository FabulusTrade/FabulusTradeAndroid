package ru.fabulus.fabulustrade.mvp.view.registration.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.util.EmailValidation

@StateStrategyType(AddToEndStrategy::class)
interface ResetPasswordView : MvpView {
    fun init()
    fun setEmailError(validation: EmailValidation)
    fun showSuccessDialog()
    fun showAlertDialog()
}