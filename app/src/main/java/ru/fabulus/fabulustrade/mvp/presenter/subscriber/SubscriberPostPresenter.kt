package ru.fabulus.fabulustrade.mvp.presenter.subscriber

import io.reactivex.rxjava3.core.Single
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter

class SubscriberPostPresenter : TraderMePostPresenter() {

    override val listPresenter = SubscriberRVListPresenter()

    inner class SubscriberRVListPresenter : TraderMePostPresenter.TraderMeRVListPresenter()

    var flashFilter = false

    override fun getPostsFromRepository(pageToLoad: Int): Single<Pagination<Post>>? {
        return apiRepo.getPostsFollowerAndObserving(profile.token!!, pageToLoad, flashFilter)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
        }
    }
}