package ru.fabulus.fabulustrade.mvp.presenter.traderme

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import com.github.terrakok.cicerone.ResultListenerHandler
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import retrofit2.HttpException
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.api.ResponseSetFlashedPost
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import ru.fabulus.fabulustrade.mvp.model.entity.exception.NoInternetException
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter.Companion.DELETE_POST_RESULT
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter.Companion.NEW_POST_RESULT
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter.Companion.UPDATE_POST_IN_FRAGMENT_RESULT
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter.Companion.UPDATE_POST_RESULT
import ru.fabulus.fabulustrade.mvp.presenter.adapter.TraderMePostRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.PostItemView
import ru.fabulus.fabulustrade.mvp.view.trader.TraderMePostView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.*
import javax.inject.Inject

class TraderMePostPresenter : MvpPresenter<TraderMePostView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var buttonsState = ButtonsState(ButtonsState.Mode.PUBLICATIONS, false)
    private var limitOfNewFlashPosts: Int = 0
    private var loadingPostDisposable: Disposable? = null
    private var gettingLimitOfFlashPostsDisposable: Disposable? = null
    private var nextPage: Int? = 1
    private var newPostResultListener: ResultListenerHandler? = null
    private var updatePostResultListener: ResultListenerHandler? = null
    private var deletePostResultListener: ResultListenerHandler? = null

    class ButtonsState(
        val mode: Mode,
        val flashedPostsOnlyFilter: Boolean
    ) {
        enum class Mode {
            PUBLICATIONS, SUBSCRIPTION
        }
    }

    private fun ButtonsState.setNewStateOfButtons(
        mode: ButtonsState.Mode = this.mode,
        flashedPostsOnlyFilter: Boolean = this.flashedPostsOnlyFilter
    ) = ButtonsState(mode, flashedPostsOnlyFilter)
        .also {
            buttonsState = it
            viewState.setBtnsState(buttonsState)
        }

    val listPresenter = TraderRVListPresenter()

    companion object {
        private const val TAG = "TraderMePostPresenter"
    }

    inner class TraderRVListPresenter : TraderMePostRVListPresenter {
        val postList = mutableListOf<Post>()
        private val tag = "TraderMePostPresenter"
        private var sharedView: PostItemView? = null

        override fun getCount(): Int = postList.size

        override fun bind(view: PostItemView) {
            val post = postList[view.pos]
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
                initFlashIcon(post)
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

        private fun PostItemView.initFlashIcon(post: Post) {
            setFlashVisibility(
                yoursPublication(post) &&
                        (post.isFlashed ||
                                isCanFlashPostByCreateDate(post.dateCreate) &&
                                limitOfNewFlashPosts > 0)
            )
            setFlashColor(post, this)
        }

        private fun yoursPublication(post: Post): Boolean {
            return post.traderId == profile.user?.id
        }

        override fun postLiked(view: PostItemView) {
            val post = postList[view.pos]
            apiRepo.likePost(profile.token!!, post.id).observeOn(AndroidSchedulers.mainThread())
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
            val post = postList[view.pos]
            apiRepo.dislikePost(profile.token!!, post.id)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
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
            val post = postList[view.pos]
            if (isCanDeletePost(post.dateCreate)) {

                apiRepo.deletePost(profile.token!!, post.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        removePostAt(view.pos)
                    }, {})
            } else {
                viewState.showToast(resourceProvider.getStringResource(R.string.post_can_not_be_deleted))
            }
        }

        private fun removePostAt(pos: Int) {
            listPresenter.postList.removeAt(pos)
            viewState.updateAdapter()
        }

        override fun editPost(view: PostItemView, post: Post) {
            if (isCanEditPost(post.dateCreate)) {
                updatePostResultListener =
                    router.setResultListener(UPDATE_POST_RESULT) { updatedPost ->
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
            listPresenter.postList[pos] = updatedPost
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
                router.setResultListener(UPDATE_POST_IN_FRAGMENT_RESULT) { updatedPost ->
                    (updatedPost as? Post)?.let {
                        updatedPostAt(view.pos, updatedPost)
                    }
                }

            deletePostResultListener =
                router.setResultListener(DELETE_POST_RESULT) {
                    removePostAt(view.pos)
                }
            router.navigateTo(Screens.postDetailFragment(postList[view.pos]))
        }

        override fun incRepostCount() {
            if (sharedView != null) {
                val post = postList[sharedView!!.pos]

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

        override fun toFlash(view: PostItemView) {
            val post = postList[view.pos]
            if (!post.isFlashed) {
                viewState.showQuestionToFlashDialog {
                    completeFlashing(post, view)
                }
            } else {
                viewState.showMessagePostIsFlashed()
            }
        }

        private fun completeFlashing(
            post: Post,
            view: PostItemView
        ) {
            profile.token?.let { token ->
                apiRepo
                    .setFlashedPost(token, post)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { response ->
                            processSuccessfulResponseOnFlashing(post, view, response)
                        },
                        { exception ->
                            processErrorOnFlashing(exception)
                        })
            }
        }

        private fun processSuccessfulResponseOnFlashing(
            post: Post,
            view: PostItemView,
            response: ResponseSetFlashedPost
        ) {
            setFlashColor(post, view)
            viewState.showMessagePostIsFlashed()
            post.isFlashed = true
            updatePostOnView(post)
            limitOfNewFlashPosts = response.flashLimit ?: 0
            if (limitOfNewFlashPosts == 0) {
                viewState.updateAdapter()
                viewState.showToast(resourceProvider.getStringResource(R.string.message_flash_limit_exhausted))
            }
        }

        private fun processErrorOnFlashing(exception: Throwable) {
            getLimitOfNewFlashPosts()
            val errorMessage = when (exception) {
                is HttpException -> {
                    exception
                        .extractResponse<ResponseSetFlashedPost>()
                        ?.message
                }
                is NoInternetException -> {
                    resourceProvider.getStringResource(R.string.message_flash_offline_exception)
                }
                else -> null
            }
                ?: resourceProvider.getStringResource(R.string.message_flash_other_exception)
            viewState.showToast(errorMessage)
        }

        private fun updatePostOnView(post: Post) {
            val pos: Int = listPresenter.postList.indexOfFirst { it.id == post.id }
            updatedPostAt(pos, post)
        }

        private fun setFlashColor(
            post: Post,
            view: PostItemView
        ) {
            if (post.isFlashed) {
                resourceProvider.getColor(R.color.colorGreen)
            } else {
                resourceProvider.getColor(R.color.colorBlue)
            }.let { view.setFlashColor(it) }
        }

        override fun share(view: PostItemView, imageViewIdList: List<ImageView>) {
            sharedView = view
            viewState.share(
                resourceProvider.getSharePostIntent(
                    postList[view.pos],
                    imageViewIdList
                )
            )
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadPosts()

        getLimitOfNewFlashPosts()
    }

    private fun getLimitOfNewFlashPosts() {
        gettingLimitOfFlashPostsDisposable?.dispose()
        gettingLimitOfFlashPostsDisposable = profile.token?.let { token ->
            profile.user?.let { user ->
                apiRepo
                    .getTraderById(token, user.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ trader ->
                        limitOfNewFlashPosts = trader.flashLimit
                        viewState.updateAdapter()
                    }, {
                        it.printStackTrace()
                    })
            }
        }
    }

    fun onScrollLimit() {
        loadPosts()
    }

    fun onCreatePostBtnClicked() {
        newPostResultListener = router.setResultListener(NEW_POST_RESULT) { post ->
            (post as? Post)?.let {
                listPresenter.postList.add(0, post)
                viewState.updateAdapter()
            }
        }
        router.navigateTo(
            Screens.createPostScreen(
                post = null,
                isPublication = true,
                isPinnedEdit = false,
                pinnedText = null
            )
        )
    }

    fun publicationsBtnClicked() {
        buttonsState.setNewStateOfButtons(
            mode = ButtonsState.Mode.PUBLICATIONS,
            flashedPostsOnlyFilter = false
        )
        initNewLoadingPosts()
        loadPosts()
    }

    fun subscriptionBtnClicked() {
        buttonsState.setNewStateOfButtons(
            mode = ButtonsState.Mode.SUBSCRIPTION,
            flashedPostsOnlyFilter = false
        )
        initNewLoadingPosts()
        loadPosts()
    }

    fun flashedPostsBtnClicked() {
        buttonsState.setNewStateOfButtons(
            flashedPostsOnlyFilter = !buttonsState.flashedPostsOnlyFilter
        )
        loadPosts(true)
    }

    fun backClicked(): Boolean {
        if (buttonsState.flashedPostsOnlyFilter) {
            buttonsState.setNewStateOfButtons(flashedPostsOnlyFilter = false)
            initNewLoadingPosts()
            loadPosts()
            return true
        }
        return false
    }

    private fun initNewLoadingPosts() {
        viewState.detachAdapter()
        nextPage = 1
        listPresenter.postList.clear()
    }

    private fun loadPosts(forceLoading: Boolean = false) {
        loadingPostDisposable?.dispose()
        if (nextPage != null || forceLoading) {
            loadingPostDisposable = getPostsFromRepositoryByState(forceLoading)
                ?.let { single ->
                    single.observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ pag ->
                            displayResultOfLoad(pag, forceLoading)
                        }, {
                            it.printStackTrace()
                        })
                }
        }
    }

    private fun displayResultOfLoad(pag: Pagination<Post>, forceLoading: Boolean) {
        if (buttonsState.flashedPostsOnlyFilter) {
            if (pag.results.isEmpty()) {
                viewState.showToast(resourceProvider.getStringResource(R.string.no_posted_posts))
                buttonsState.setNewStateOfButtons(flashedPostsOnlyFilter = false)
                if (listPresenter.postList.isEmpty()) {
                    initNewLoadingPosts()
                    loadPosts()
                }
                return
            }
        }
        if (forceLoading) {
            initNewLoadingPosts()
        }
        val itIsFirstLoadingPosts = listPresenter.postList.isEmpty()
        listPresenter.postList.addAll(pag.results)
        if (itIsFirstLoadingPosts) {
            viewState.attachAdapter()
        } else {
            viewState.updateAdapter()
        }
        nextPage = pag.next
    }

    private fun getPostsFromRepositoryByState(forceLoading: Boolean) =
        profile.token?.let { token ->
            val pageToLoad = if (forceLoading) {
                1
            } else {
                nextPage ?: 1
            }
            with(apiRepo) {
                if (buttonsState.mode == ButtonsState.Mode.SUBSCRIPTION) {
                    getPostsFollowerAndObserving(
                        token,
                        pageToLoad,
                        buttonsState.flashedPostsOnlyFilter
                    )
                } else {
                    getMyPosts(token, pageToLoad, buttonsState.flashedPostsOnlyFilter)
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        newPostResultListener?.dispose()
        updatePostResultListener?.dispose()
        deletePostResultListener?.dispose()
        loadingPostDisposable?.dispose()
        gettingLimitOfFlashPostsDisposable?.dispose()
    }
}