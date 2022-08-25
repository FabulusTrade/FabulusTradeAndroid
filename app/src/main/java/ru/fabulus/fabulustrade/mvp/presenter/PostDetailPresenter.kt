package ru.fabulus.fabulustrade.mvp.presenter

import android.graphics.Color
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.isCanFlashPostByCreateDate
import ru.fabulus.fabulustrade.util.isNegativeDigit

class PostDetailPresenter(override var post: Post) : BasePostPresenter<PostDetailView>(post) {

    companion object {
        private const val TAG = "PostDetailPresenter"
    }

    override fun onFirstViewAttach() {
        App.instance.appComponent.inject(this)
        super.onFirstViewAttach()

        initMenu()

        setHeadersIcons()

        viewState.setCurrentUserAvatar(profile.user!!.avatar!!)

        viewState.setRepostCount(post.repostCount.toString())
    }

    private fun setHeadersIcons() {
        viewState.initFooterFlash(post.isFlashed)
        viewState.setHeaderFlashVisibility(isVisible = isSelfPost(post) && isCanFlashPostByCreateDate(post.dateCreate))
        viewState.setHeaderFlashColor(isFlashed = post.isFlashed)
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
}