package ru.fabulus.fabulustrade.mvp.presenter.traderme

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
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
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

    private var state = State.PUBLICATIONS
    private var flashedPostsOnlyFilter = false
    private var loadingPostDisposable: Disposable? = null
    private var nextPage: Int? = 1
    private var newPostResultListener: ResultListenerHandler? = null
    private var updatePostResultListener: ResultListenerHandler? = null
    private var deletePostResultListener: ResultListenerHandler? = null

    enum class State {
        PUBLICATIONS, SUBSCRIPTION
    }

    val listPresenter = TraderRVListPresenter()

    inner class TraderRVListPresenter : TraderMePostRVListPresenter {
        val postList = mutableListOf<Post>()
        private val tag = "TraderMePostPresenter"
        private var sharedView: PostItemView? = null

        override fun getCount(): Int = postList.size

        override fun bind(view: PostItemView) {
            val post = postList[view.pos]
            initView(view, post)
            initMenu(view, post)
        }

        private fun initMenu(view: PostItemView, post: Post) {
            if (yoursPublication(post)) {
                view.setIvAttachedKebabMenuSelf(post)
            } else {
                view.setIvAttachedKebabMenuSomeone(post)
            }
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
            setFlashVisibility(yoursPublication(post))
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

        private fun updatedPostAt(pos: Int, updatedPost: Post) {
            listPresenter.postList[pos] = updatedPost
            viewState.updateAdapter()
        }

        override fun copyPost(post: Post) {
            resourceProvider.copyToClipboard(post.text)
            viewState.showToast(resourceProvider.getStringResource(R.string.text_copied))
        }

        override fun complainOnPost(post: Post, reason: String) {
            //TODO метод для отправки жалобы
            viewState.showComplainSnackBar()
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
                viewState.showMessagePostIsPosted()
            }
        }

        private fun completeFlashing(
            post: Post,
            view: PostItemView
        ) {
            post.isFlashed = true
            setFlashColor(post, view)
            viewState.showMessagePostIsPosted()
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
                postId = null,
                isPublication = true,
                isPinnedEdit = false,
                pinnedText = null
            )
        )
    }

    fun publicationsBtnClicked() {
        state = State.PUBLICATIONS
        initNewLoadingPosts()
        loadPosts()
    }

    fun subscriptionBtnClicked() {
        state = State.SUBSCRIPTION
        initNewLoadingPosts()
        loadPosts()
    }

    fun flashedPostsBtnClicked() {
        if (!flashedPostsOnlyFilter) {
            flashedPostsOnlyFilter = true
            loadPosts(true)
        } else {
            viewState.showToast(resourceProvider.getStringResource(R.string.message_flash_filter_is_set))
        }
    }

    fun backClicked(): Boolean {
        if (flashedPostsOnlyFilter) {
            flashedPostsOnlyFilter = false
            initNewLoadingPosts()
            loadPosts()
            return true
        }
        return false
    }

    private fun initNewLoadingPosts() {
        viewState.setBtnsState(state)
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
                            processResultOfLoad(pag)
                        }, {
                            it.printStackTrace()
                        })
                }
        }
    }

    private fun processResultOfLoad(pag: Pagination<Post>) {
        if (flashedPostsOnlyFilter) {
            if (pag.results.isEmpty()) {
                viewState.showToast(resourceProvider.getStringResource(R.string.no_posted_posts))
                flashedPostsOnlyFilter = false
                if (listPresenter.postList.isEmpty()) {
                    initNewLoadingPosts()
                    loadPosts()
                }
                return
            }
            initNewLoadingPosts()
        }
        listPresenter.postList.addAll(pag.results)
        viewState.updateAdapter()
        nextPage = pag.next
    }

    private fun getPostsFromRepositoryByState(forceLoading: Boolean) = profile.token?.let { token ->
        val pageToLoad = if (forceLoading) {
            null
        } else {
            nextPage
        } ?: 1
        with(apiRepo) {
            if (state == State.SUBSCRIPTION) {
                getPostsFollowerAndObserving(token, pageToLoad, flashedPostsOnlyFilter)
            } else {
                getMyPosts(token, pageToLoad, flashedPostsOnlyFilter)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        newPostResultListener?.dispose()
        updatePostResultListener?.dispose()
        deletePostResultListener?.dispose()
        loadingPostDisposable?.dispose()
    }
}