package ru.wintrade.mvp.presenter.trader

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.PostRVListPresenter
import ru.wintrade.mvp.view.item.PostItemView
import ru.wintrade.mvp.view.trader.TraderPostView
import ru.wintrade.navigation.Screens
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

    val listPresenter = TraderRVListPresenter()

    inner class TraderRVListPresenter : PostRVListPresenter {
        val posts = mutableListOf<Post>()
        override fun getCount(): Int = posts.size

        override fun bind(view: PostItemView) {
            val post = posts[view.pos]
            view.setNewsDate(post.dateCreate)
            view.setPost(post.text)
            view.setImages(post.images)
            view.setLikesCount(post.likeCount)
            view.setDislikesCount(post.dislikeCount)
        }

        override fun postLiked(view: PostItemView) {
            val post = posts[view.pos]
            post.like()
            view.setLikesCount(post.likeCount)
            apiRepo.likePost(profile.token!!, post.id).subscribe()
        }

        override fun postDisliked(view: PostItemView) {
            val post = posts[view.pos]
            post.dislike()
            view.setDislikesCount(post.dislikeCount)
            apiRepo.dislikePost(profile.token!!, post.id).subscribe()
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
            apiRepo.getTraderPosts(profile.token!!, trader.id, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.posts.addAll(pag.results)
                    viewState.updateAdapter()
                    nextPage = pag.next
                }, {
                    it.printStackTrace()
                })
        }
    }

    fun onScrollLimit() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            apiRepo.getTraderPosts(profile.token!!, trader.id, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.posts.addAll(pag.results)
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
        router.navigateTo(Screens.SignInScreen())
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.SignUpScreen())
    }
}