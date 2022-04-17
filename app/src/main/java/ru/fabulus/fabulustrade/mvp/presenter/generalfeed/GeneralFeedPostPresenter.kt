package ru.fabulus.fabulustrade.mvp.presenter.generalfeed

import io.reactivex.rxjava3.core.Single
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter

class GeneralFeedPostPresenter : TraderMePostPresenter() {
    override fun getPostsFromRepository(pageToLoad: Int): Single<Pagination<Post>>? =
            apiRepo.getPostsForGeneralNews(profile.token, pageToLoad)
}
