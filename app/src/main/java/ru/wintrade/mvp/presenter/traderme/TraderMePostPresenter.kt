package ru.wintrade.mvp.presenter.traderme

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
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
            view.setNewsDate(post.dateCreate)
            view.setPost(post.text)
            view.setImages(post.images)
            view.setLikesCount(post.likeCount)
            view.setDislikesCount(post.dislikeCount)
        }

        override fun postLiked(view: PostItemView) {
            val post = post[view.pos]
            post.like()
            view.setLikesCount(post.likeCount)
            apiRepo.likePost(profile.token!!, post.id).subscribe()
        }

        override fun postDisliked(view: PostItemView) {
            val post = post[view.pos]
            post.dislike()
            view.setDislikesCount(post.dislikeCount)
            apiRepo.dislikePost(profile.token!!, post.id).subscribe()
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
        router.navigateTo(Screens.CreatePostScreen(false, null))
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
                apiRepo.getAllPosts(profile.token!!, nextPage!!)
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