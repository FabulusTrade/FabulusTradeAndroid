package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            }, {
                it.printStackTrace()
            })
    }
}
