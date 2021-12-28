package ru.fabulus.fabulustrade.mvp.view.item

interface CommentItemView {
    var pos: Int

    fun setCommentText(text: String)
    fun setCommentAuthorAvatar(avatarUrl: String)
    fun setCommentAuthorUserName(userName: String)
    fun setCommentDateText(text: String)
    fun setLikeCountText(text: String)
    fun setLikeImageActive()
    fun setLikeImageInactive()
}