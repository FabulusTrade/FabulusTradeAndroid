package ru.fabulus.fabulustrade.mvp.view.item

interface CommentItemView {
    var pos: Int

    fun setCommentText(text: String)
    fun setCommentAuthorAvatar(avatarUrl: String)
}