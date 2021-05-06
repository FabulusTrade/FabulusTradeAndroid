package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.wintrade.mvp.view.SettingsView

class SettingsPresenter : MvpPresenter<SettingsView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}