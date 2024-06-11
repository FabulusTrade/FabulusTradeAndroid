package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SetPasswordSecondViewModel @Inject constructor() : BaseProfileEditingViewModel() {

    private val successMessageTextInApiResponse =
        "Адрес электронной почты изменен. Письмо подтверждения отправлено."
    var code = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    var passwordConfirmation = mutableStateOf("")
        private set

    fun updateCode(newCode: String) {
        code.value = newCode
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updatePasswordConfirmation(newPassword: String) {
        passwordConfirmation.value = newPassword
    }

    fun changePassword() {
        apiRepo.сhangePasswordByCode(profile.token!!, code.value, password.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseSendPasswordChangeEmail ->
                if (responseSendPasswordChangeEmail.message.equals(successMessageTextInApiResponse)) {
                    _successMessage.value = responseSendPasswordChangeEmail.message
                } else {
                    _errorMessage.value = responseSendPasswordChangeEmail.message
                }
            }, { throwable ->
                val errorMsg = parseErrorMessage(throwable)
                _errorMessage.value = errorMsg
            })
    }
}
