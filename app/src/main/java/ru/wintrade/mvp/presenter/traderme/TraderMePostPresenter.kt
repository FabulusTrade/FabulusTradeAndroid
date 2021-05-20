package ru.wintrade.mvp.presenter.traderme

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITraderNewsRVListPresenter
import ru.wintrade.mvp.view.item.TraderNewsItemView
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
    }

    fun onViewResumed() {
        listPresenter.news.clear()
        nextPage = 1
        loadPosts()
    }

    fun onScrollLimit() {
        loadPosts()
    }

    fun onCreatePostBtnClicked() {
        router.navigateTo(Screens.CreatePostScreen(false))
    }

    fun publicationsBtnClicked() {
        state = State.PUBLICATIONS
        viewState.setBtnsState(state)
        nextPage = 1
        listPresenter.news.clear()
        loadPosts()
    }

    fun subscriptionBtnClicked() {
        state = State.SUBSCRIPTION
        viewState.setBtnsState(state)
        nextPage = 1
        listPresenter.news.clear()
        loadPosts()
    }

    private fun loadPosts() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            if (state == State.SUBSCRIPTION) {
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
            } else {
                apiRepo.getMyPosts(profile.token!!, nextPage!!)
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
    }
}