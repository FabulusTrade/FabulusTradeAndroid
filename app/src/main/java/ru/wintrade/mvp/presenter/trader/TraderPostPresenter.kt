package ru.wintrade.mvp.presenter.trader

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITraderNewsRVListPresenter
import ru.wintrade.mvp.view.item.TraderNewsItemView
import ru.wintrade.mvp.view.trader.TraderPostView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TraderPostPresenter : MvpPresenter<TraderPostView>() {

    private var isLoading = false
    private var nextPage: Int? = 1

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var router: Router

    val listPresenter = TraderNewsRVListPresenter()

    inner class TraderNewsRVListPresenter : ITraderNewsRVListPresenter {
        val news = mutableListOf<Post>()
        override fun getCount(): Int = news.size

        override fun bind(view: TraderNewsItemView) {
            val newsList = news[view.pos]
            view.setNewsDate(newsList.dateCreate)
            view.setPost(newsList.text)
            view.setImages(newsList.images)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
            apiRepo.getAllPosts(profile.token!!, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.news.addAll(pag.results)
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
            apiRepo.getAllPosts(profile.token!!, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.news.addAll(pag.results)
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