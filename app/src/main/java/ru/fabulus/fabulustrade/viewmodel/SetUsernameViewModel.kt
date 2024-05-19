package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import retrofit2.HttpException
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import javax.inject.Inject


class SetUsernameViewModel @Inject constructor() : ViewModel() {

    private val SUCCESS_MESSAGE = "Никнейм успешно обновлен"
    var nickname = mutableStateOf("")
        private set

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage = _successMessage.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo
    fun updateNickname(newNickname: String) {
        nickname.value = newNickname
    }

    fun saveNickname() {
        apiRepo.setUsername(profile.token!!, nickname.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseSetUsername ->
                if (responseSetUsername.message.equals(SUCCESS_MESSAGE)) {
                    _successMessage.value = responseSetUsername.message
                } else {
                    _errorMessage.value = responseSetUsername.message
                }
            }, {throwable ->
                val errorMsg = parseErrorMessage(throwable)
                _errorMessage.value = errorMsg
            })
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    private fun parseErrorMessage(throwable: Throwable): String {
        return if (throwable is HttpException) {
            val responseBody = throwable.response()?.errorBody()
            responseBody?.let {
                return try {
                    val jsonObject = JSONObject(it.string())
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Неизвестная ошибка"
                }
            } ?: "An unknown error occurred"
        } else {
            throwable.message ?: "Неизвестная ошибка"
        }
    }
}
