package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.navigation.Screens
import javax.inject.Inject


class SetPasswordFirstViewModel @Inject constructor() : BaseProfileEditingViewModel() {

    @Inject
    lateinit var router: Router

    private val successMessageTextInApiResponse =
        "Код отправлен на указанный почтовый ящик"
    var email = mutableStateOf("")
        private set

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun openPasswordSecondScreen() {
        router.navigateTo(Screens.setPasswordSecondScreen())
    }
    fun sendEmail() {
        apiRepo.sendPasswordChangeEmail(profile.token!!, email.value)
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
