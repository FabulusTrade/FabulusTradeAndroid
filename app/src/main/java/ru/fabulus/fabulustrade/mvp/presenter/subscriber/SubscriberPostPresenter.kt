package ru.fabulus.fabulustrade.mvp.presenter.subscriber

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.PostRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.PostItemView
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberNewsView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.*
import javax.inject.Inject

class SubscriberPostPresenter : MvpPresenter<SubscriberNewsView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var isLoading = false
    private var nextPage: Int? = 1

    val listPresenter = TraderRVListPresenter()

    inner class TraderRVListPresenter : PostRVListPresenter {
        val postList = mutableListOf<Post>()
        private val tag = "TraderRVListPresenter"
        private var sharedItemPosition: Int? = null

        override fun getCount(): Int = postList.size

        override fun bind(view: PostItemView) {
            val post = postList[view.pos]
            initView(view, post)
        }

        fun incRepostCount() {
            if (sharedItemPosition != null) {
                val post = postList[sharedItemPosition!!]
                sharedItemPosition = null
                apiRepo
                    .incRepostCount(post.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ incPostResult ->
                        if (incPostResult.result.equals("ok", true)) {
                            post.incRepostCount()
                        } else {
                            viewState.showToast(incPostResult.message)
                        }
                    }, { error ->
                        Log.d(
                            tag,
                            "Error incRepostCount: ${error.message.toString()}"
                        )
                        Log.d(tag, error.printStackTrace().toString())
                    }
                    )
            }
        }

        override fun share(position: Int, imageViewIdList: List<ImageView>) {
            sharedItemPosition = position
            viewState.share(
                resourceProvider.getSharePostIntent(
                    postList[position],
                    imageViewIdList
                )
            )
        }

        private fun initView(view: PostItemView, post: Post) {
            with(view) {
                setNewsDate(post.dateCreate)
                setPost(post.text)
                setImages(post.images)
                setLikeImage(post.isLiked)
                setDislikeImage(post.isDisliked)
                setLikesCount(post.likeCount)
                setDislikesCount(post.dislikeCount)
                setKebabMenuVisibility(yoursPublication(post))
                setProfileName(post.userName)
                setProfileAvatar(post.avatarUrl)
                val commentCount = post.commentCount()
                setCommentCount(
                    resourceProvider.formatQuantityString(
                        R.plurals.show_comments_count_text,
                        commentCount,
                        commentCount
                    )
                )
                setProfit(
                    resourceProvider.formatDigitWithDef(
                        R.string.tv_profit_percent_text,
                        post.colorIncrDecrDepo365.value
                    ),
                    Color.parseColor(post.colorIncrDecrDepo365.color)
                )

                if (post.colorIncrDecrDepo365.value?.isNegativeDigit() == true) {
                    setProfitNegativeArrow()
                } else {
                    setProfitPositiveArrow()
                }

                setAuthorFollowerCount(
                    resourceProvider.formatDigitWithDef(
                        R.string.tv_author_follower_count,
                        post.followersCount
                    )
                )

                setRepostCount(post.repostCount.toString())
            }
        }

        private fun yoursPublication(post: Post): Boolean {
            return post.traderId == profile.user?.id
        }

        override fun postLiked(view: PostItemView) {
            val post = postList[view.pos]

            apiRepo
                .likePost(profile.token!!, post.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    post.like()
                    if (post.isDisliked) {
                        view.setDislikeImage(!post.isDisliked)
                        view.setDislikesCount(post.dislikeCount - 1)
                    }
                    view.setLikesCount(post.likeCount)
                    view.setLikeImage(post.isLiked)
                }, {
                    // Ошибка не обрабатывается
                })
        }

        override fun postDisliked(view: PostItemView) {
            val post = postList[view.pos]

            apiRepo
                .dislikePost(profile.token!!, post.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    post.dislike()
                    if (post.isLiked) {
                        view.setLikeImage(!post.isLiked)
                        view.setLikesCount(post.likeCount - 1)
                    }
                    view.setDislikesCount(post.dislikeCount)
                    view.setDislikeImage(post.isDisliked)
                }, {
                    // Ошибка не обрабатывается
                })
        }

        override fun postDelete(view: PostItemView) {
            //nothing
        }

        override fun postUpdate(view: PostItemView) {
            //nothing
        }

        override fun setPublicationTextMaxLines(view: PostItemView) {
            view.isOpen = !view.isOpen
            view.setPublicationItemTextMaxLines(view.isOpen)
        }

        override fun showCommentDetails(view: PostItemView) {
            router.navigateTo(Screens.postDetailFragment(postList[view.pos]))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun onViewResumed() {
        listPresenter.postList.clear()
        nextPage = 1
        loadPosts()
    }

    fun onScrollLimit() {
        loadPosts()
    }

    private fun loadPosts() {
        if (nextPage != null && !isLoading) {
            isLoading = true

            apiRepo
                .getPostsFollowerAndObserving(profile.token!!, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    if (pag.results.isEmpty()) {
                        viewState.withoutSubscribeAnyTrader()
                        return@subscribe
                    }
                    listPresenter.postList.addAll(pag.results)
                    viewState.updateAdapter()
                    nextPage = pag.next
                    isLoading = false
                }, {
                    it.printStackTrace()
                    isLoading = false
                })
        }
    }
}