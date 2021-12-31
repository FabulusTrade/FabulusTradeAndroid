package ru.fabulus.fabulustrade.mvp.presenter

import android.graphics.Color
import android.util.Log
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
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatQuantityString
import ru.fabulus.fabulustrade.util.isNegativeDigit
import ru.fabulus.fabulustrade.util.toStringFormat
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

    fun sharePost() {

    }

}