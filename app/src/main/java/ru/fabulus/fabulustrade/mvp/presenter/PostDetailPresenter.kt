package ru.fabulus.fabulustrade.mvp.presenter

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.text.HtmlCompat
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

    inner class CommentPostDetailPresenter : CommentRVListPresenter {

        private val commentList = mutableListOf<Comment>()
        override fun getCount(): Int = commentList.size

        override fun bind(view: CommentItemView) {
            initView(view, commentByPos(view.pos))
        }

        private fun commentByPos(pos: Int): Comment = commentList[pos]

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

                if (comment.parentCommentId != null) {
                    setCommentText(
                        comment.text.toSpannableText(
                            0,
                            getParentCommentAuthorUsername(comment.parentCommentId!!).length + 1,
                            resourceProvider.getColor(R.color.author_color_in_comment)
                        )
                    )
                } else {
                    setCommentText(comment.text.toSpannable())
                }

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
            }
        }

        override fun setCommentList(commentList: MutableList<Comment>) {
            if (this.commentList.count() > 0) {
                this.commentList.clear()
            }
            this.commentList.addAll(commentList)
        }

        override fun replyOnComment(view: CommentItemView) {
            val comment = commentByPos(view.pos)
            parentCommentId = comment.id

            val textStr = resourceProvider.formatString(
                R.string.reply_on_comment_pattern,
                comment.authorUsername
            )
            val text = textStr.toSpannableText(
                0,
                textStr.length,
                resourceProvider.getColor(R.color.author_color_in_comment)
            )

            viewState.prepareReplyToComment(text)
        }

        private fun getParentCommentAuthorUsername(parentCommentId: Long): String {
            for (comment in commentList) {
                if (comment.id == parentCommentId) {
                    return comment.authorUsername ?: ""
                }
            }

            return ""
        }

        override fun recalcParentCommentId(commentText: String) {
            if (parentCommentId != null && commentText.indexOf(
                    getParentCommentAuthorUsername(
                        parentCommentId!!
                    )
                ) != 0
            ) {
                parentCommentId = null
            }
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

        viewState.setPostImages(post.images)
        viewState.setPostLikeCount(post.likeCount.toString())
        viewState.setPostDislikeCount(post.dislikeCount.toString())

        setLikeImage(post.isLiked)
        setDislikeImage(post.isDisliked)

        setCommentCount()
        setCommentList()
        viewState.setCurrentUserAvatar(profile.user!!.avatar!!)
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
                viewState.clearNewCommentText()
            }, { error ->
                Log.d(TAG, "Error: ${error.message.toString()}")
                Log.d(TAG, error.printStackTrace().toString())
            }
            )
    }

    fun getParentCommentId() = parentCommentId

    fun sharePost(imageViewIdList: List<ImageView>) {

        var bmpUri: Uri? = null
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/html"

            val title = HtmlCompat.fromHtml(
                resourceProvider.formatString(
                    R.string.share_message_pattern,
                    post.text
                ), HtmlCompat.FROM_HTML_MODE_COMPACT
            )

            putExtra(Intent.EXTRA_TEXT, title)
            if (post.images.count() > 0) {
                bmpUri = imageViewIdList[0].getBitmapUriFromDrawable()
                if (bmpUri != null) {
                    putExtra(Intent.EXTRA_STREAM, bmpUri)

                    type = "image/*"
                }
            }

        }

        val chooser = Intent.createChooser(
            shareIntent,
            resourceProvider.getStringResource(R.string.share_message_title)
        )

        if (bmpUri != null) {
            imageViewIdList[0].context.let { context ->
                val resInfoList: List<ResolveInfo> = context.packageManager
                    .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    context.grantUriPermission(
                        packageName,
                        bmpUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }

        }

        viewState.sharePost(chooser)

    }

}