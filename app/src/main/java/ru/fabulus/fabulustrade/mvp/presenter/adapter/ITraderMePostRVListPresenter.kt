package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.PostItemView

interface ITraderMePostRVListPresenter : IPostWithBlacklistRVListPresenter {
    fun toFlash(view: PostItemView)
}