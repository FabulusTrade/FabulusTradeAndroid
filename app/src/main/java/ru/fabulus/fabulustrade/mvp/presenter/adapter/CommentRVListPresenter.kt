package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.model.entity.Comment
import ru.fabulus.fabulustrade.mvp.view.item.CommentItemView

interface CommentRVListPresenter {
    fun getCount(): Int
    fun bind(view: CommentItemView)
    fun likeComment(view: CommentItemView)
    fun setCommentList(commentList: MutableList<Comment>)
}