package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.model.entity.Comment
import ru.fabulus.fabulustrade.mvp.view.item.CommentItemView

interface CommentRVListPresenter {
    fun getCount(): Int
    fun bind(view: CommentItemView)
    fun likeComment(view: CommentItemView)
    fun setCommentList(commentList: MutableList<Comment>)
    fun replyOnComment(view: CommentItemView)
    fun recalcParentComment(commentText: String)
    fun editComment(view: CommentItemView, comment: Comment)
    fun copyComment(comment: Comment)
    fun deleteComment(view: CommentItemView, comment: Comment)
    fun complainOnComment(comment: Comment, reason: String)
    fun commentByPos(position: Int): Comment
    fun updateCommentItem(position: Int, comment: Comment)
}