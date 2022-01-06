package ru.fabulus.fabulustrade.mvp.presenter

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.text.HtmlCompat
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
                setCommentText(comment.text)
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

    fun addPostComment(text: String, parentCommentId: Int? = null) {
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
            val resInfoList: List<ResolveInfo> = imageViewIdList[0].context.packageManager
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                imageViewIdList[0].context.grantUriPermission(
                    packageName,
                    bmpUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }

        viewState.sharePost(chooser)

    }

}