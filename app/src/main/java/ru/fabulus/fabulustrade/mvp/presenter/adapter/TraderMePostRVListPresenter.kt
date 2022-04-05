package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.PostItemView

interface TraderMePostRVListPresenter : PostRVListPresenter {
    fun toFlash(view: PostItemView)
}