package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SetEmailViewModel @Inject constructor() : BaseProfileEditingViewModel() {

    private val successMessageTextInApiResponse =
        "Письмо подтверждения отправлено на новый адрес электронной почты."
    var email = mutableStateOf("")
        private set

    val previousEmail by lazy {
        mutableStateOf(profile.user?.email ?: "")
    }

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun saveEmail() {
        apiRepo.setEmail(profile.token!!, email.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseSetEmail ->
                if (responseSetEmail.message.equals(successMessageTextInApiResponse)) {
                    _successMessage.value = responseSetEmail.message
                    reloadProfile()
                } else {
                    _errorMessage.value = responseSetEmail.message
                }
            }, { throwable ->
                val errorMsg = parseErrorMessage(throwable)
                _errorMessage.value = errorMsg
            })
    }
}
