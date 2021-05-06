package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.wintrade.mvp.view.FriendInviteView

class FriendInvitePresenter : MvpPresenter<FriendInviteView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}