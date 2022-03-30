package ru.fabulus.fabulustrade.mvp.presenter

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import com.github.terrakok.cicerone.ResultListenerHandler
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.mvp.view.item.CommentItemView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.*
import javax.inject.Inject

class PostDetailPresenter(var post: Post) : MvpPresenter<PostDetailView>() {

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

    lateinit var listPresenter: CommentPostDetailPresenter

    private val maxCommentLength = 200
    private var updatePostResultListener: ResultListenerHandler? = null

    override fun onFirstViewAttach() {
        App.instance.appComponent.inject(this)
        super.onFirstViewAttach()
        listPresenter = CommentPostDetailPresenter(viewState, post)
        viewState.init()
        initMenu()
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

        setCommentCount()
        setCommentList()
        viewState.setCurrentUserAvatar(profile.user!!.avatar!!)

        viewState.setRepostCount(post.repostCount.toString())
    }

    private fun setHeadersIcons() {
        viewState.setFlashVisibility(isSelfPost(post))
        viewState.setProfitAndFollowersVisibility(!isSelfPost(post))

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

    private fun initMenu() {
        if (isSelfPost(post)) {
            viewState.setPostMenuSelf(post)
        } else {
            fillComplaints()
        }
    }

    private fun fillComplaints() {
        apiRepo
            .getComplaints(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ complaintList ->
                viewState.setPostMenuSomeone(post, complaintList)
            }, { error ->
                Log.d(TAG, "Error: ${error.message.toString()}")
                Log.d(TAG, error.printStackTrace().toString())
            }
            )
    }

    private fun isSelfPost(post: Post): Boolean {
        return post.traderId == profile.user?.id
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

    fun share(imageViewIdList: List<ImageView>) {
        viewState.share(
            resourceProvider.getSharePostIntent(
                post,
                imageViewIdList
            )
        )

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

    override fun onDestroy() {
        super.onDestroy()
        updatePostResultListener?.dispose()
    }

}