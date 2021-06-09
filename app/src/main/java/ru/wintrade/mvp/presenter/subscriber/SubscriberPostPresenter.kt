package ru.wintrade.mvp.presenter.subscriber

import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.PostRVListPresenter
import ru.wintrade.mvp.view.item.PostItemView
import ru.wintrade.mvp.view.subscriber.SubscriberNewsView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class SubscriberPostPresenter : MvpPresenter<SubscriberNewsView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    private var isLoading = false
    private var nextPage: Int? = 1

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

    private fun loadPosts() {
        if (nextPage != null && !isLoading) {
            isLoading = true
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
        }
    }
}