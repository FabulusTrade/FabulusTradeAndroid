package ru.fabulus.fabulustrade.mvp.presenter

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.util.Log
import android.widget.ImageView
import androidx.core.text.toSpannable
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Comment
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.CommentRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.mvp.view.item.CommentItemView
import ru.fabulus.fabulustrade.util.*
import javax.inject.Inject

class PostDetailPresenter(val post: Post) : MvpPresenter<PostDetailView>() {

    companion object {
        private const val TAG = "PostDetailPresenter"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    val listPresenter = CommentPostDetailPresenter()
    private var parentCommentId: Long? = null
    private var parentCommentAuthorUsername: String? = null
    private var parentCommentView: CommentItemView? = null
    private var updatedCommentView: CommentItemView? = null
    private val maxCommentLength = 200

    inner class CommentPostDetailPresenter : CommentRVListPresenter {

        private val commentList = mutableListOf<Comment>()
        override fun getCount(): Int = commentList.size

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

            viewState.prepareReplyToComment(text, maxCommentLength)
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
                viewState.prepareUpdateComment(prepareCommentText(comment), maxCommentLength)
            } else {
                viewState.showToast(resourceProvider.getStringResource(R.string.comment_can_not_be_edited))
            }
        }

        override fun copyComment(comment: Comment) {
            resourceProvider.copyToClipboard(comment.text)
            viewState.showToast(resourceProvider.getStringResource(R.string.text_copied))
        }

        override fun deleteComment(comment: Comment) {
            if (isCanDeleteComment(comment.dateCreate)) {
                //TODO метод для удаления коментария
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setPostAuthorAvatar(post.avatarUrl)
        viewState.setPostAuthorName(post.userName)
        viewState.setPostDateCreated(post.dateCreate.toStringFormat())
        viewState.setPostText(post.text)
        viewState.setProfit(
            resourceProvider.formatDigitWithDef(
                R.string.tv_profit_percent_text,
                post.colorIncrDecrDepo365.value
            ),
            Color.parseColor(post.colorIncrDecrDepo365.color)
        )

        if (post.colorIncrDecrDepo365.value?.isNegativeDigit() == true) {
            viewState.setProfitNegativeArrow()
        } else {
            viewState.setProfitPositiveArrow()
        }

        viewState.showSendComment(maxCommentLength)

        viewState.setPostImages(post.images)
        viewState.setPostLikeCount(post.likeCount.toString())
        viewState.setPostDislikeCount(post.dislikeCount.toString())

        setLikeImage(post.isLiked)
        setDislikeImage(post.isDisliked)

        setCommentCount()
        setCommentList()
        viewState.setCurrentUserAvatar(profile.user!!.avatar!!)
        viewState.setAuthorFollowerCount(
            resourceProvider.formatDigitWithDef(
                R.string.tv_author_follower_count,
                post.followersCount
            )
        )
    }

    private fun setCommentList() {
        listPresenter.setCommentList(post.comments)
        viewState.updateCommentsAdapter()
    }

    private fun setCommentCount() {
        viewState.setCommentCount(
            resourceProvider.formatQuantityString(
                R.plurals.show_comments_count_text,
                post.commentCount(),
                post.commentCount()
            )
        )
    }

    private fun setDislikeImage(isDisliked: Boolean) {
        if (isDisliked) {
            viewState.setDislikeActiveImage()
        } else {
            viewState.setDislikeInactiveImage()
        }
    }

    private fun setLikeImage(isLiked: Boolean) {
        if (isLiked) {
            viewState.setLikeActiveImage()
        } else {
            viewState.setLikeInactiveImage()
        }
    }

    fun likePost() {
        apiRepo
            .likePost(profile.token!!, post.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                post.like()
                if (post.isDisliked) {
                    setDislikeImage(!post.isDisliked)
                    viewState.setPostDislikeCount((post.dislikeCount - 1).toString())
                }
                viewState.setPostLikeCount(post.likeCount.toString())
                setLikeImage(post.isLiked)
            }, {})
    }

    fun dislikePost() {
        apiRepo
            .dislikePost(profile.token!!, post.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                post.dislike()
                if (post.isLiked) {
                    setLikeImage(!post.isLiked)
                    viewState.setPostLikeCount((post.likeCount - 1).toString())
                }
                viewState.setPostDislikeCount(post.dislikeCount.toString())
                setDislikeImage(post.isDisliked)
            }, {})
    }

    fun changeSendCommentButton(text: String) {
        if (text.trim().isEmpty()) {
            viewState.setUnclickableSendCommentBtn()
        } else {
            viewState.setClickableSendCommentBtn()
        }
    }

    fun changeUpdateCommentButton(text: String) {
        if (text.trim().isEmpty()) {
            viewState.setUnclickableUpdateCommentBtn()
        } else {
            viewState.setClickableUpdateCommentBtn()
        }
    }

    private fun addPostComment(text: String, parentCommentId: Long? = null) {
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
                Log.d(TAG, "Error: ${error.message.toString()}")
                Log.d(TAG, error.printStackTrace().toString())
            }
            )
    }

    fun sendComment(text: String) {
        text.let { commentText ->
            listPresenter.recalcParentComment(commentText)
            addPostComment(
                commentText,
                getParentCommentId()
            )
        }
    }

    fun updateComment(text: String) {
        if (updatedCommentView != null) {
            val pos = updatedCommentView!!.pos
            val comment = listPresenter.commentByPos(pos)
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

                        listPresenter.updateCommentItem(pos, newComment)
                        viewState.showSendComment(maxCommentLength)
                    }, { error ->
                        Log.d(
                            TAG,
                            "Error in PostDetailPresenter 'updateComment': ${error.message.toString()}"
                        )
                        Log.d(TAG, error.printStackTrace().toString())
                    }
                    )
            }
            updatedCommentView = null
        }
    }

    fun getParentCommentId() = parentCommentId

    fun sharePost(imageViewIdList: List<ImageView>) {
        var bmpUri: Uri? = null
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"

            var title = resourceProvider.formatString(
                R.string.share_message_pattern,
                post.userName,
                post.text
            )

            if (title.length > MAX_SHARED_LEN_POST_TEXT) {
                title = resourceProvider.formatString(
                    R.string.share_message_pattern_big_text,
                    title.substring(0, MAX_SHARED_LEN_POST_TEXT)
                )
            }

            putExtra(Intent.EXTRA_TEXT, title)
            if (post.images.count() > 0) {
                imageViewIdList[0].getBitmapUriFromDrawable()?.let { uri ->
                    bmpUri = uri
                    putExtra(Intent.EXTRA_STREAM, bmpUri)
                    type = "image/*"
                }
            }
        }

        val chooser = Intent.createChooser(
            shareIntent,
            resourceProvider.getStringResource(R.string.share_message_title)
        )

        bmpUri?.let { uri ->
            imageViewIdList[0].context.let { context ->
                val resInfoList: List<ResolveInfo> = context.packageManager
                    .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    context.grantUriPermission(
                        packageName,
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }

        }

        viewState.sharePost(chooser)

    }

    fun closeUpdateComment() {
        viewState.showSendComment(maxCommentLength)
    }

}