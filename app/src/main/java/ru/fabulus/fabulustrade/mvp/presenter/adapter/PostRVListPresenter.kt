package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.PostItemView

interface PostRVListPresenter {
    fun getCount(): Int
    fun bind(view: PostItemView)
    fun postLiked(view: PostItemView)
    fun postDisliked(view: PostItemView)
    fun postDelete(view: PostItemView)
    fun postUpdate(view: PostItemView)
    fun setPublicationTextMaxLines(view: PostItemView)
}