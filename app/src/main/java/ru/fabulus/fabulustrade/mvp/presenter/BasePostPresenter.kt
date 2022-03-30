package ru.fabulus.fabulustrade.mvp.presenter

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import com.github.terrakok.cicerone.ResultListenerHandler
import com.github.terrakok.cicerone.Router
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.BasePostView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.*
import javax.inject.Inject

class BasePostPresenter(var post: Post) : MvpPresenter<BasePostView>() {

    companion object {
        const val TAG = "BasePostPresenter"
        const val maxCommentLength = 200
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    lateinit var listPresenter: CommentPostDetailPresenter

    private var updatePostResultListener: ResultListenerHandler? = null

    override fun onFirstViewAttach() {
        App.instance.appComponent.inject(this)
        super.onFirstViewAttach()
        listPresenter = CommentPostDetailPresenter(viewState, post)
        viewState.init()
        viewState.setPostAuthorAvatar(post.avatarUrl)
        viewState.setPostAuthorName(post.userName)
        viewState.setPostDateCreated(post.dateCreate.toStringFormat())
        viewState.setPostText(post.text)

        viewState.showSendComment(maxCommentLength)

        viewState.setPostImages(post.images)
        viewState.setPostLikeCount(post.likeCount.toString())
        viewState.setPostDislikeCount(post.dislikeCount.toString())

        setLikeImage(post.isLiked)
        setDislikeImage(post.isDisliked)
        setHeadersIcons()

        listPresenter.setCommentCount()
        setCommentList()
        viewState.setCurrentUserAvatar(profile.user!!.avatar!!)

    }

    private fun setHeadersIcons() {
        if (!isSelfPost(post)) {
            setProfit()
            setFollowersCount()
        }
    }

    private fun setProfit() {
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
    }

    private fun setFollowersCount() {
        viewState.setAuthorFollowerCount(
            resourceProvider.formatDigitWithDef(
                R.string.tv_author_follower_count,
                post.followersCount
            )
        )
    }

    private fun isSelfPost(post: Post): Boolean {
        return post.traderId == profile.user?.id
    }

    private fun setCommentList() {
        listPresenter.setCommentList(post.comments)
        viewState.updateCommentsAdapter()
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

    fun sendComment(text: String) {
        text.let { commentText ->
            listPresenter.recalcParentComment(commentText)
            listPresenter.addPostComment(
                commentText,
                listPresenter.getParentCommentId()
            )
        }
    }

    fun updateComment(text: String) {
        listPresenter.updateComment(text)
    }

    fun incRepostCount() {
        apiRepo
            .incRepostCount(post.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ incPostResult ->
                if (incPostResult.result.equals("ok", true)) {
                    post.incRepostCount()
                }
            }, { error ->
                Log.d(TAG, "Error incRepostCount: ${error.message.toString()}")
                Log.d(TAG, error.printStackTrace().toString())
            }
            )
    }

    fun share(imageViewIdList: List<ImageView>) {
        viewState.share(
            resourceProvider.getSharePostIntent(
                post,
                imageViewIdList
            )
        )

    }

    fun closeUpdateComment() {
        viewState.showSendComment(maxCommentLength)
    }

    fun deletePost() {
        if (isCanDeletePost(post.dateCreate)) {
            apiRepo.deletePost(profile.token!!, post.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    router.sendResult(CreatePostPresenter.DELETE_POST_RESULT, "")
                    router.exit()
                }, {})
        } else {
            viewState.showToast(resourceProvider.getStringResource(R.string.post_can_not_be_deleted))
        }
    }

    fun editPost() {
        if (isCanEditPost(post.dateCreate)) {
            updatePostResultListener =
                router.setResultListener(CreatePostPresenter.UPDATE_POST_RESULT) { updatedPost ->
                    (updatedPost as? Post)?.let {
                        post = updatedPost
                        viewState.setPostText(post.text)
                        viewState.setPostImages(post.images)
                        router.sendResult(
                            CreatePostPresenter.UPDATE_POST_IN_FRAGMENT_RESULT,
                            updatedPost
                        )
                    }
                }

            router.navigateTo(
                Screens.createPostScreen(
                    post.id.toString(),
                    true,
                    null,
                    post.text
                )
            )
        } else {
            viewState.showToast(resourceProvider.getStringResource(R.string.post_can_not_be_edited))
        }
    }

    fun copyPost() {
        resourceProvider.copyToClipboard(post.text)
        viewState.showToast(resourceProvider.getStringResource(R.string.text_copied))
    }

    fun complainOnPost(complaintId: Int) {
        apiRepo.complainOnPost(profile.token!!, post.id, complaintId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showComplainSnackBar()
            }, {})
    }

    override fun onDestroy() {
        super.onDestroy()
        updatePostResultListener?.dispose()
    }
}