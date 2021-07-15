package ru.wintrade.mvp.presenter.traderme

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.PostRVListPresenter
import ru.wintrade.mvp.view.item.PostItemView
import ru.wintrade.mvp.view.trader.TraderMePostView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TraderMePostPresenter : MvpPresenter<TraderMePostView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    private var state = State.PUBLICATIONS
    private var isLoading = false
    private var nextPage: Int? = 1

    enum class State {
        PUBLICATIONS, SUBSCRIPTION
    }

    val listPresenter = TraderRVListPresenter()

    inner class TraderRVListPresenter : PostRVListPresenter {
        val post = mutableListOf<Post>()

        override fun getCount(): Int = post.size

        override fun bind(view: PostItemView) {
            val post = post[view.pos]
            initView(view, post)
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
            }
        }

        private fun yoursPublication(post: Post): Boolean {
            return post.traderId == profile.user?.id
        }

        override fun postLiked(view: PostItemView) {
            val post = post[view.pos]
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
            val post = post[view.pos]
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

        override fun postDelete(view: PostItemView) {
            val post = post[view.pos]
            apiRepo.deletePost(profile.token!!, post.id).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listPresenter.post.clear()
                    nextPage = 1
                    loadPosts()
                }, {})
        }

        override fun postUpdate(view: PostItemView) {
            val post = post[view.pos]
            router.navigateTo(Screens.CreatePostScreen(post.id.toString(), true, false, post.text))
        }

        override fun setPublicationTextMaxLines(view: PostItemView) {
            view.isOpen = !view.isOpen
            view.setPublicationItemTextMaxLines(view.isOpen)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun onViewResumed() {
        listPresenter.post.clear()
        nextPage = 1
        loadPosts()
    }

    fun onScrollLimit() {
        loadPosts()
    }

    fun onCreatePostBtnClicked() {
        router.navigateTo(
            Screens.CreatePostScreen(
                postId = null,
                isPublication = true,
                isPinnedEdit = false,
                pinnedText = null
            )
        )
    }

    fun publicationsBtnClicked() {
        state = State.PUBLICATIONS
        viewState.setBtnsState(state)
        nextPage = 1
        listPresenter.post.clear()
        loadPosts()
    }

    fun subscriptionBtnClicked() {
        state = State.SUBSCRIPTION
        viewState.setBtnsState(state)
        nextPage = 1
        listPresenter.post.clear()
        loadPosts()
    }

    private fun loadPosts() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            if (state == State.SUBSCRIPTION) {
                apiRepo.getPublisherPosts(profile.token!!, nextPage!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ pag ->
                        listPresenter.post.addAll(pag.results)
                        viewState.updateAdapter()
                        nextPage = pag.next
                        isLoading = false
                    }, {
                        it.printStackTrace()
                        isLoading = false
                    })
            } else {
                apiRepo.getMyPosts(profile.token!!, nextPage!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ pag ->
                        listPresenter.post.addAll(pag.results)
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
}