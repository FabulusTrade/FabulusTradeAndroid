package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SetFirstAndLastNamesViewModel @Inject constructor() : BaseProfileEditingViewModel() {

    private val successMessageTextInApiResponse = "Имя и фамилия пользователя обновлены успешно"
    var firstName = mutableStateOf("")
        private set

    var lastName = mutableStateOf("")
        private set

    fun updateFirstName(newFirstName: String) {
        firstName.value = newFirstName
    }

    fun updateLastName(newLastName: String) {
        lastName.value = newLastName
    }

    fun saveFirstAndLastName() {
        apiRepo.setFirstAndLastNames(profile.token!!, firstName.value, lastName.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseSetFirstAndLastName ->
                if (responseSetFirstAndLastName.message.equals(successMessageTextInApiResponse)) {
                    _successMessage.value = responseSetFirstAndLastName.message
                } else {
                    _errorMessage.value = responseSetFirstAndLastName.message
                }
            }, { throwable ->
                val errorMsg = parseErrorMessage(throwable)
                _errorMessage.value = errorMsg
            })
    }
}
