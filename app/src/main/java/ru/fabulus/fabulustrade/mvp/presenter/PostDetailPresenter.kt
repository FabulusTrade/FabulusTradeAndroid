package ru.fabulus.fabulustrade.mvp.presenter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.isNegativeDigit
import ru.fabulus.fabulustrade.util.toStringFormat
import javax.inject.Inject

class PostDetailPresenter(val post: Post) : MvpPresenter<PostDetailView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

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

    fun sharePost() {

    }

}