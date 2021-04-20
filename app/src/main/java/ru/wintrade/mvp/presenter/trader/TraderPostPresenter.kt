package ru.wintrade.mvp.presenter.trader

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITraderNewsRVListPresenter
import ru.wintrade.mvp.view.item.TraderNewsItemView
import ru.wintrade.mvp.view.trader.TraderPostView
import javax.inject.Inject

class TraderPostPresenter : MvpPresenter<TraderPostView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profileStorage: ProfileStorage

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
        viewState.setVisibility(true)
    }

    fun onScrollLimit() {
        loadPosts()
    }

    fun publicationsBtnClicked() {
        state = State.PUBLICATIONS
        viewState.setBtnsState(state)
        viewState.setVisibility(true)
    }

    fun subscriptionBtnClicked() {
        state = State.SUBSCRIPTION
        viewState.setBtnsState(state)
        viewState.setVisibility(false)
        loadPosts()
    }

    private fun loadPosts() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            profileStorage.profile?.let {
                apiRepo.getAllPosts(it.token, nextPage!!)
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