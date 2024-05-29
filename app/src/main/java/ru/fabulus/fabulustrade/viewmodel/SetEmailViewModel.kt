package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SetEmailViewModel @Inject constructor() : BaseProfileEditingViewModel() {

    private val successMessageTextInApiResponse = "Адрес электронной почты изменен. Письмо подтверждения отправлено."
    var nickname = mutableStateOf("")
        private set

    fun updateNickname(newNickname: String) {
        nickname.value = newNickname
    }

    fun saveNickname() {
        apiRepo.setEmail(profile.token!!, nickname.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseSetEmail ->
                if (responseSetEmail.message.equals(successMessageTextInApiResponse)) {
                    _successMessage.value = responseSetEmail.message
                } else {
                    _errorMessage.value = responseSetEmail.message
                }
            }, { throwable ->
                val errorMsg = parseErrorMessage(throwable)
                _errorMessage.value = errorMsg
            })
    }
}
