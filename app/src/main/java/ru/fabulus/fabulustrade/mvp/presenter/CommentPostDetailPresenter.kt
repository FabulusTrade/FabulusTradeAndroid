package ru.fabulus.fabulustrade.mvp.presenter

import android.text.Spannable
import android.util.Log
import androidx.core.text.toSpannable
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Comment
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.CommentRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.BasePostView
import ru.fabulus.fabulustrade.mvp.view.item.CommentItemView
import ru.fabulus.fabulustrade.util.*
import javax.inject.Inject

class CommentPostDetailPresenter (val viewState: BasePostView, val post: Post) : CommentRVListPresenter {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    private var parentCommentAuthorUsername: String? = null
    private var parentCommentView: CommentItemView? = null

    private var updatedCommentView: CommentItemView? = null

    private val commentList = mutableListOf<Comment>()
    override fun getCount(): Int = commentList.size

    private var parentCommentId: Long? = null

    fun getParentCommentId() = parentCommentId

    override fun bind(view: CommentItemView) {
        initView(view, commentByPos(view.pos))
        initMenu(view, commentByPos(view.pos))
    }

    private fun initMenu(view: CommentItemView, comment: Comment) {

        if (isSelfComment(comment)) {
            view.setBtnCommentMenuSelf(comment)
        } else {
            view.setBtnCommentMenuSomeone(comment)
        }
    }

    override fun commentByPos(position: Int): Comment = commentList[position]

    override fun likeComment(view: CommentItemView) {
        val comment = commentByPos(view.pos)
        apiRepo
            .likeComment(profile.token!!, comment.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (comment.isLiked == true) {
                    view.setLikeImageInactive()
                    comment.unlike()
                } else {
                    view.setLikeImageActive()
                    comment.like()
                }

                view.setLikeCountText(comment.likeCount.toString())
            }, {})
    }

    private fun initView(view: CommentItemView, comment: Comment) {
        with(view) {

            setCommentText(prepareCommentText(comment))

            comment.avatarUrl?.let { avatarUrl -> setCommentAuthorAvatar(avatarUrl) }
            comment.authorUsername?.let { authorUsername ->
                setCommentAuthorUserName(
                    authorUsername
                )
            }
            setCommentDateText(
                comment.dateCreate.toStringFormat(
                    resourceProvider.getStringResource(
                        R.string.comment_date_create_format
                    )
                )
            )
            setLikeCountText(comment.likeCount.toString())
            if (comment.isLiked == true) {
                view.setLikeImageActive()
            } else {
                view.setLikeImageInactive()
            }

            if (isSelfComment(comment)) {
                hideReplyBtn()
            }
        }
    }

    private fun prepareCommentText(comment: Comment): Spannable {
        return if (comment.parentCommentId != null) {
            comment.text.toSpannableText(
                0,
                getParentCommentAuthorUsername(comment.parentCommentId!!).length + 1,
                resourceProvider.getColor(R.color.author_color_in_comment)
            )
        } else {
            comment.text.toSpannable()
        }
    }

    private fun isSelfComment(comment: Comment): Boolean =
        (comment.authorUuid == profile.user?.id)

    fun addPostComment(text: String, parentCommentId: Long? = null) {
        apiRepo
            .addPostComment(profile.token!!, post.id, text, parentCommentId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ comment ->
                profile.user?.let { user ->
                    comment.authorUsername = user.username
                    comment.avatarUrl = user.avatar
                    comment.authorUuid = user.id
                }
                post.comments.add(comment)
                setCommentCount()
                setCommentList()
                viewState.setRvPosition(post.commentCount() - 1)
                viewState.scrollNsvCommentViewToBottom()
                viewState.clearNewCommentText()
            }, { error ->
                Log.d(BasePostPresenter.TAG, "Error: ${error.message.toString()}")
                Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
            }
            )
    }

    fun setCommentCount() {
        viewState.setCommentCount(
            resourceProvider.formatQuantityString(
                R.plurals.show_comments_count_text,
                post.commentCount(),
                post.commentCount()
            )
        )
    }

    private fun setCommentList() {
        setCommentList(post.comments)
        viewState.updateCommentsAdapter()
    }

    override fun setCommentList(commentList: MutableList<Comment>) {
        if (this.commentList.count() > 0) {
            this.commentList.clear()
        }
        this.commentList.addAll(commentList)
    }

    override fun replyOnComment(view: CommentItemView) {
        val comment = commentByPos(view.pos)
        if (parentCommentId != null) {
            clearParentComment()
        }
        parentCommentId = comment.id
        parentCommentAuthorUsername = comment.authorUsername
        parentCommentView = view

        val textStr = resourceProvider.formatString(
            R.string.reply_on_comment_pattern,
            parentCommentAuthorUsername
        )
        val text = textStr.toSpannableText(
            0,
            textStr.length,
            resourceProvider.getColor(R.color.author_color_in_comment)
        )

        viewState.prepareReplyToComment(text, BasePostPresenter.maxCommentLength)
        view.setReplyPostColor(resourceProvider.getColor(R.color.cv_comment_header_background_color_reply))
    }

    private fun getParentCommentAuthorUsername(parentCommentId: Long): String {
        for (comment in commentList) {
            if (comment.id == parentCommentId) {
                return comment.authorUsername ?: ""
            }
        }

        return ""
    }

    override fun recalcParentComment(commentText: String) {
        if (parentCommentId != null && commentText.indexOf(parentCommentAuthorUsername!!) != 1
        ) {
            clearParentComment()
        }
    }

    private fun clearParentComment() {
        parentCommentId = null
        parentCommentView!!.setReplyPostColor(resourceProvider.getColor(R.color.cv_comment_header_background_color))
        parentCommentView = null
    }

    override fun editComment(view: CommentItemView, comment: Comment) {
        if (isCanEditComment(comment.dateCreate)) {
            updatedCommentView = view
        } else {
            viewState.prepareUpdateComment(prepareCommentText(comment), BasePostPresenter.maxCommentLength)
            viewState.showToast(resourceProvider.getStringResource(R.string.comment_can_not_be_edited))
        }
    }

    fun updateComment(text: String) {
        if (updatedCommentView != null) {
            val pos = updatedCommentView!!.pos
            val comment = commentByPos(pos)
            if (comment.text != text) {
                apiRepo
                    .updateComment(profile.token!!, comment.id, text)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ commentTmp ->

                        val newComment = Comment(
                            comment.id,
                            comment.postId,
                            comment.parentCommentId,
                            comment.authorUuid,
                            comment.authorUsername,
                            comment.avatarUrl,
                            commentTmp.text,
                            commentTmp.dateCreate,
                            commentTmp.dateUpdate,
                            comment.likeCount,
                            comment.dislikeCount,
                            comment.isLiked
                        )
                        post.comments[pos] = newComment

                        updateCommentItem(pos, newComment)
                        viewState.showSendComment(BasePostPresenter.maxCommentLength)
                    }, { error ->
                        Log.d(
                            BasePostPresenter.TAG,
                            "Error in PostDetailPresenter 'updateComment': ${error.message.toString()}"
                        )
                        Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
                    }
                    )
            }
            updatedCommentView = null
        }
    }

    override fun copyComment(comment: Comment) {
        resourceProvider.copyToClipboard(comment.text)
        viewState.showToast(resourceProvider.getStringResource(R.string.text_copied))
    }

    override fun deleteComment(view: CommentItemView, comment: Comment) {
        if (isCanDeleteComment(comment.dateCreate)) {
            apiRepo
                .deleteComment(profile.token!!, comment.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ deleteCommentResult ->
                    if (deleteCommentResult.result.equals("ok", true)) {
                        post.comments.removeAt(view.pos)
                        setCommentCount()
                        setCommentList()
                        viewState.setRvPosition(view.pos - 1)
                        viewState.showToast(resourceProvider.getStringResource(R.string.comment_deleted))
                    } else {
                        deleteCommentResult.message?.let { message ->
                            viewState.showToast(
                                resourceProvider.formatString(
                                    R.string.error_comment_deleted,
                                    message
                                )
                            )
                        }
                    }
                }, { error ->
                    viewState.showToast(resourceProvider.getStringResource(R.string.request_error_comment_deleted))
                    Log.d(BasePostPresenter.TAG, "Error: ${error.message.toString()}")
                    Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
                }
                )
        } else {
            viewState.showToast(resourceProvider.getStringResource(R.string.comment_can_not_be_deleted))
        }
    }

    override fun complainOnComment(comment: Comment, reason: String) {
        //TODO метод для отправки жалобы
        viewState.showComplainSnackBar()
    }

    override fun updateCommentItem(position: Int, comment: Comment) {
        this.commentList[position] = comment
        viewState.notifyItemChanged(position)
    }
}