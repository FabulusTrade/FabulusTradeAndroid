package ru.fabulus.fabulustrade.mvp.presenter.subscriber

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
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter
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
    private var isShareResumeAction: Boolean = false
    private var updatePostResultListener: ResultListenerHandler? = null
    private var deletePostResultListener: ResultListenerHandler? = null
    val listPresenter = TraderRVListPresenter()

    inner class TraderRVListPresenter : PostRVListPresenter {
        val postList = mutableListOf<Post>()
        private val tag = "TraderRVListPresenter"
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


        override fun incRepostCount() {
            if (sharedView != null) {
                val post = postList[sharedView!!.pos]
                isShareResumeAction = true
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
            TODO("Not yet implemented")
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
                    router.setResultListener(CreatePostPresenter.UPDATE_POST_RESULT) { updatedPost ->
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
                router.setResultListener(CreatePostPresenter.UPDATE_POST_IN_FRAGMENT_RESULT) { updatedPost ->
                    (updatedPost as? Post)?.let {
                        updatedPostAt(view.pos, updatedPost)
                    }
                }

            deletePostResultListener =
                router.setResultListener(CreatePostPresenter.DELETE_POST_RESULT) {
                    removePostAt(view.pos)
                }
            router.navigateTo(Screens.postDetailFragment(postList[view.pos]))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun onViewResumed() {
        if (!isShareResumeAction) {
            listPresenter.postList.clear()
            nextPage = 1
            loadPosts()
        } else {
            isShareResumeAction = false
        }
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

    override fun onDestroy() {
        super.onDestroy()
        updatePostResultListener?.dispose()
        deletePostResultListener?.dispose()
    }
}