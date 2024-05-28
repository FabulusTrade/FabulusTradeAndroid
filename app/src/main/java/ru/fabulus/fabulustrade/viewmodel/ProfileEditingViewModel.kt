package ru.fabulus.fabulustrade.viewmodel

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import ru.fabulus.fabulustrade.navigation.Screens
import javax.inject.Inject


class ProfileEditingViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var router: Router

    fun openUsernameEditor() {
        router.navigateTo(Screens.setUsernameScreen())
    }

    fun openFirstAndLastNamesEditor() {
        router.navigateTo(Screens.setFirstAndLastNamesScreen())
    }

}