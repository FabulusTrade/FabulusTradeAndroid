package ru.fabulus.fabulustrade.mvp.view.item

import android.text.Spannable
import ru.fabulus.fabulustrade.mvp.model.entity.Comment
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint

interface CommentItemView {
    var pos: Int

    fun setCommentText(text: Spannable)
    fun setCommentAuthorAvatar(avatarUrl: String)
    fun setCommentAuthorUserName(userName: String)
    fun setCommentDateText(text: String)
    fun setLikeCountText(text: String)
    fun setLikeImageActive()
    fun setLikeImageInactive()
    fun setReplyPostColor(backgroundColor: Int)
    fun setBtnCommentMenuSelf(comment: Comment)
    fun setBtnCommentMenuSomeone(comment: Comment, complaintList: List<Complaint>)
    fun hideReplyBtn()
}