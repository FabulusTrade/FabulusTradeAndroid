package ru.fabulus.fabulustrade.mvp.presenter.trader

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import com.github.terrakok.cicerone.ResultListenerHandler
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.BasePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.PostItemView
import ru.fabulus.fabulustrade.mvp.view.trader.TraderPostView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatQuantityString
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.getSharePostIntent
import ru.fabulus.fabulustrade.util.isCanDeletePost
import ru.fabulus.fabulustrade.util.isCanEditPost
import ru.fabulus.fabulustrade.util.isNegativeDigit
import ru.fabulus.fabulustrade.util.toStringFormat
import javax.inject.Inject

class TraderPostPresenter(val trader: Trader) : MvpPresenter<TraderPostView>() {

    private var isLoading = false
    private var nextPage: Int? = 1

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var flashFilterIs: Boolean = false
    private var loadingPostDisposable: Disposable? = null
    val listPresenter = TraderRVListPresenter()
    private var updatePostResultListener: ResultListenerHandler? = null
    private var deletePostResultListener: ResultListenerHandler? = null

    companion object {
        private const val TAG = "TraderPostPresenter"
    }

    inner class TraderRVListPresenter : IPostRVListPresenter {
        val posts = mutableListOf<Post>()
        private val tag = "TraderPostPresenter"
        private var sharedView: PostItemView? = null

        override fun getCount(): Int = posts.size

        override fun bind(view: PostItemView) {
            val post = posts[view.pos]
            initView(view, post)
            initMenu(view, post)
            initButtonVisibility(view)
        }

        private fun initMenu(view: PostItemView, post: Post) {
            if (yoursPublication(post)) {
                view.setIvAttachedKebabMenuSelf(post)
            } else {
                fillComplaints(view, post)
            }
        }

        private fun initButtonVisibility(view: PostItemView) {
            view.getCountLineAndSetButtonVisibility()
        }

        private fun fillComplaints(view: PostItemView, post: Post) {
            apiRepo
                .getComplaints(profile.token!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ complaintList ->
                    view.setIvAttachedKebabMenuSomeone(post, complaintList)
                }, { error ->
                    Log.d(TAG, "Error: ${error.message.toString()}")
                    Log.d(TAG, error.printStackTrace().toString())
                }
                )
        }

        override fun incRepostCount() {
            if (sharedView != null) {
                val post = posts[sharedView!!.pos]

                apiRepo
                    .incRepostCount(post.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ incPostResult ->
                        if (incPostResult.result.equals("ok", true)) {
                            post.incRepostCount()
                            sharedView!!.setRepostCount(post.repostCount.toString())
                        } else {
                            viewState.showToast(incPostResult.message)
                        }
                        sharedView = null
                    }, { error ->
                        Log.d(
                            tag,
                            "Error incRepostCount: ${error.message.toString()}"
                        )
                        Log.d(tag, error.printStackTrace().toString())
                        sharedView = null
                    }
                    )
            }
        }

        override fun navigateToTraderScreen(position: Int) {
            val clickedTraderId = listPresenter.posts[position].traderId
            if (trader.id != clickedTraderId) {
                apiRepo.getTraderById(
                    token = profile.token!!,
                    traderId = clickedTraderId,
                ).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { trader ->
                            router.navigateTo(Screens.traderMainScreen(trader))
                        },
                        {
                            it.printStackTrace()
                        }
                    )
            }
        }

        override fun share(view: PostItemView, imageViewIdList: List<ImageView>) {
            sharedView = view
            viewState.share(
                resourceProvider.getSharePostIntent(
                    posts[view.pos],
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
                setProfileName(post.userName)
                setProfileAvatar(post.avatarUrl)
                setHeaderIcon(view, post)
                val commentCount = post.commentCount()
                setCommentCount(
                    resourceProvider.formatQuantityString(
                        R.plurals.show_comments_count_text,
                        commentCount,
                        commentCount
                    )
                )

                setRepostCount(post.repostCount.toString())
            }
        }

        private fun setHeaderIcon(view: PostItemView, post: Post) {
            with(view) {
                setFlashVisibility(yoursPublication(post))
                initFlashFooterVisibility(post.isFlashed)
                setFlashColor(post, this)
                setProfitAndFollowersVisibility(!yoursPublication(post))
                if (!yoursPublication(post)) {
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
                }
            }
        }

        private fun setFlashColor(
            post: Post,
            view: PostItemView,
        ) {
            if (post.isFlashed) {
                resourceProvider.getColor(R.color.colorGreen)
            } else {
                resourceProvider.getColor(R.color.colorBlue)
            }.let {
                view.setFlashColor(it)
                view.initFlashFooterColor(it)
            }
        }

        private fun yoursPublication(post: Post): Boolean {
            return post.traderId == profile.user?.id
        }

        override fun postLiked(view: PostItemView) {
            val post = posts[view.pos]

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
                }, {})
        }

        override fun postDisliked(view: PostItemView) {
            val post = posts[view.pos]

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
                }, {})
        }

        override fun deletePost(view: PostItemView) {
            val post = posts[view.pos]
            if (isCanDeletePost(post.dateCreate)) {
                apiRepo
                    .getBlockUserInfo(profile.token!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ blockUserInfo ->
                        if (blockUserInfo.isEnabledOperationsOnPost()) {
                            delPost(post, view.pos)
                        } else {
                            viewState.showToast(
                                resourceProvider.formatString(
                                    R.string.block_operation_on_post,
                                    blockUserInfo.postsBlockTime.toStringFormat(
                                        "dd.MM.yyyy"
                                    )
                                )
                            )
                        }
                    }, { error ->
                        // приходит ошибка 401 если пользователь никогда не блокировался
                        delPost(post, view.pos)

                        Log.d(BasePostPresenter.TAG, "Error: ${error.message.toString()}")
                        Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
                    }
                    )

            } else {
                viewState.showToast(resourceProvider.getStringResource(R.string.post_can_not_be_deleted))
            }
        }

        private fun delPost(post: Post, position: Int) {
            apiRepo.deletePost(profile.token!!, post.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    removePostAt(position)
                }, {})
        }

        private fun removePostAt(pos: Int) {
            listPresenter.posts.removeAt(pos)
            viewState.updateAdapter()
        }

        override fun editPost(view: PostItemView, post: Post) {
            if (isCanEditPost(post.dateCreate)) {
                updatePostResultListener =
                    router.setResultListener(CreatePostPresenter.UPDATE_POST_RESULT) { updatedPost ->
                        (updatedPost as? Post)?.let {
                            updatedPostAt(view.pos, updatedPost)
                        }
                    }

                router.navigateTo(
                    Screens.createPostScreen(
                        post,
                        true,
                        null,
                        post.text
                    )
                )
            } else {
                viewState.showToast(resourceProvider.getStringResource(R.string.post_can_not_be_edited))
            }
        }

        private fun updatedPostAt(pos: Int, updatedPost: Post) {
            listPresenter.posts[pos] = updatedPost
            viewState.updateAdapter()
        }

        override fun copyPost(post: Post) {
            resourceProvider.copyToClipboard(post.text)
            viewState.showToast(resourceProvider.getStringResource(R.string.text_copied))
        }

        override fun complainOnPost(post: Post, complaintId: Int) {
            apiRepo.complainOnPost(profile.token!!, post.id, complaintId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showComplainSnackBar()
                }, {})
        }

        override fun setPublicationTextMaxLines(view: PostItemView) {
            view.isOpen = !view.isOpen
            view.setPublicationItemTextMaxLines(view.isOpen)
        }

        override fun showCommentDetails(view: PostItemView) {
            updatePostResultListener =
                router.setResultListener(CreatePostPresenter.UPDATE_POST_IN_FRAGMENT_RESULT) { updatedPost ->
                    (updatedPost as? Post)?.let {
                        updatedPostAt(view.pos, updatedPost)
                    }
                }

            deletePostResultListener =
                router.setResultListener(CreatePostPresenter.DELETE_POST_RESULT) {
                    removePostAt(view.pos)
                }
            router.navigateTo(Screens.postDetailFragment(posts[view.pos]))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadingPostDisposable?.dispose()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
            loadingPostDisposable = apiRepo
                .getTraderPosts(profile.token!!, trader.id, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.posts.addAll(checkFlashed(pag))
                    viewState.updateAdapter()
                    nextPage = pag.next
                }, {
                    it.printStackTrace()
                })
        }
    }

    private fun checkFlashed(pag: Pagination<Post>): List<Post> {
        return if (flashFilterIs) {
            pag.results.filter { post ->
                post.isFlashed
            }
        } else {
            pag.results
        }
    }

    fun onScrollLimit() {
        loadingPostDisposable?.dispose()
        if (nextPage != null && !isLoading) {
            isLoading = true
            apiRepo
                .getTraderPosts(profile.token!!, trader.id, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.posts.addAll(checkFlashed(pag))
                    viewState.updateAdapter()
                    nextPage = pag.next
                    isLoading = false
                }, {
                    it.printStackTrace()
                    isLoading = false
                })
        }
    }

    fun openSignInScreen() {
        router.navigateTo(Screens.signInScreen(false))
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.signUpScreen(false))
    }

    fun flashIconClicked() {
        loadingPostDisposable?.dispose()
        flashFilterIs = !flashFilterIs
        loadingPostDisposable = apiRepo
            .getTraderPosts(profile.token!!, trader.id, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ pag ->
                listPresenter.posts.clear()
                listPresenter.posts.addAll(checkFlashed(pag))
                viewState.updateAdapter()
                nextPage = pag.next
            }, {
                it.printStackTrace()
            })
        viewState.setFlashIcon(flashFilterIs)
        viewState.updateAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingPostDisposable?.dispose()
        updatePostResultListener?.dispose()
        deletePostResultListener?.dispose()
    }
}