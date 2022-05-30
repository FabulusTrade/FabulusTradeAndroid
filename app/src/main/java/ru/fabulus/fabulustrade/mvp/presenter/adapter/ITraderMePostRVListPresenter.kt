package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.PostItemView

interface ITraderMePostRVListPresenter : IPostRVListPresenter {
    fun toFlash(view: PostItemView)
}