package ru.fabulus.fabulustrade.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import javax.inject.Inject

class SetUsernameViewModel @Inject constructor(): ViewModel() {
    var nickname = mutableStateOf("")
        private set

    private val _snackbarFlow = MutableSharedFlow<String>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()

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
                viewModelScope.launch {
                    _snackbarFlow.emit(responseSetUsername.message) // Сообщение, которое будет показано в SnackBar
                }
            }, {
                it.printStackTrace()
            })
    }
}
