package ru.fabulus.fabulustrade.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import retrofit2.HttpException
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import javax.inject.Inject

open class BaseProfileEditingViewModel : ViewModel() {
    protected val _successMessage = MutableStateFlow<String?>(null)
    val successMessage = _successMessage.asStateFlow()

    protected val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    @Inject
    lateinit var profile: Profile

    @Inject
    protected lateinit var apiRepo: ApiRepo

    fun setSuccessMessage(message: String?) {
        _successMessage.value = message
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    protected fun parseErrorMessage(throwable: Throwable): String {
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