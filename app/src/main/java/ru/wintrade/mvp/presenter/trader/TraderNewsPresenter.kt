package ru.wintrade.mvp.presenter.trader

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.TraderNews
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITraderNewsRVListPresenter
import ru.wintrade.mvp.view.item.TraderNewsItemView
import ru.wintrade.mvp.view.trader.TraderNewsView
import java.text.SimpleDateFormat
import javax.inject.Inject

class TraderNewsPresenter : MvpPresenter<TraderNewsView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profileStorage: ProfileStorage

    private var state = State.PUBLICATIONS

    enum class State {
        PUBLICATIONS, SUBSCRIPTION
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setVisibility(true)
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
        getNews()
    }

    val listPresenter = TraderNewsRVListPresenter()

    inner class TraderNewsRVListPresenter : ITraderNewsRVListPresenter {
        val news = mutableListOf<TraderNews>()
        override fun getCount(): Int = news.size

        override fun bind(view: TraderNewsItemView) {
            val newsList = news[view.pos]
            view.setNewsDate(newsList.dateCreate)
            view.setPost(newsList.text)
        }
    }

    private fun getNews() {
        profileStorage.profile?.let {
            apiRepo.getAllNews(it.token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listPresenter.news.clear()
                    listPresenter.news.addAll(it)
                    viewState.updateRecyclerView()
                }, {
                    it.printStackTrace()
                })
        }
    }
}