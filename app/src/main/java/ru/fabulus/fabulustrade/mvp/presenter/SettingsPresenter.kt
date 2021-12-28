package ru.fabulus.fabulustrade.mvp.presenter

import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.view.SettingsView

class SettingsPresenter : MvpPresenter<SettingsView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}