package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.wintrade.mvp.view.QuestionView

class QuestionPresenter : MvpPresenter<QuestionView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}