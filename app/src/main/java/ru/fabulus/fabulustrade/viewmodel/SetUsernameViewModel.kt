package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SetUsernameViewModel @Inject constructor() : BaseProfileEditingViewModel() {

    private val successMessageTextInApiResponse = "Никнейм успешно обновлен"
    var nickname = mutableStateOf("")
        private set

    fun updateNickname(newNickname: String) {
        nickname.value = newNickname
    }

    fun saveNickname() {
        apiRepo.setUsername(profile.token!!, nickname.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseSetUsername ->
                if (responseSetUsername.message.equals(successMessageTextInApiResponse)) {
                    _successMessage.value = responseSetUsername.message
                    reloadProfile()
                } else {
                    _errorMessage.value = responseSetUsername.message
                }
            }, { throwable ->
                val errorMsg = parseErrorMessage(throwable)
                _errorMessage.value = errorMsg
            })
    }
}
