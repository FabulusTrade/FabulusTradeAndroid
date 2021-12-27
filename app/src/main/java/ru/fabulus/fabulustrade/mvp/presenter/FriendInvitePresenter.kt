package ru.fabulus.fabulustrade.mvp.presenter

import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.view.FriendInviteView

class FriendInvitePresenter : MvpPresenter<FriendInviteView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}