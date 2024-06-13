package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SetPhoneNumberViewModel @Inject constructor() : BaseProfileEditingViewModel() {

    private val successMessageTextInApiResponse =
        "Номер телефона успешно изменен"
    var phoneNumber = mutableStateOf("")
        private set

    val previousPhoneNumber by lazy {
        mutableStateOf(profile.user?.phone ?: "")
    }

    fun updatePhoneNumber(newPhoneNumber: String) {
        phoneNumber.value = newPhoneNumber
    }

    fun savePhoneNumber() {
        apiRepo.setPhoneNumber(profile.token!!, phoneNumber.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseSetPhoneNumber ->
                if (responseSetPhoneNumber.message.equals(successMessageTextInApiResponse)) {
                    _successMessage.value = responseSetPhoneNumber.message
                    reloadProfile()
                } else {
                    _errorMessage.value = responseSetPhoneNumber.message
                }
            }, { throwable ->
                val errorMsg = parseErrorMessage(throwable)
                _errorMessage.value = errorMsg
            })
    }
}
