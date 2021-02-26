package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.wintrade.mvp.model.FragmentListData
import ru.wintrade.mvp.view.LoadingView

class LoadingPresenter : MvpPresenter<LoadingView>() {
    var model: FragmentListData = FragmentListData()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init(model.getFragmentsList())
    }
}