package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.PostItemView

interface PostRVListPresenter {
    fun getCount(): Int
    fun bind(view: PostItemView)
    fun postLiked(view: PostItemView)
    fun postDisliked(view: PostItemView)
}