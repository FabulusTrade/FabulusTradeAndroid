package ru.fabulus.fabulustrade.mvp.presenter.generalfeed

import io.reactivex.rxjava3.core.Single
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostRVListPresenter
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostWithBlacklistRVListPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.fabulus.fabulustrade.navigation.Screens

class GeneralFeedPostPresenter : TraderMePostPresenter() {
    override fun getPostsFromRepository(pageToLoad: Int): Single<Pagination<Post>>? =
        apiRepo.getPostsForGeneralNews(profile.token, pageToLoad)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
        }
    }

    inner class GeneralFeedRVListPresenter: TraderMePostPresenter.TraderMeRVListPresenter(), IPostWithBlacklistRVListPresenter {
        override fun askToAddToBlacklist(traderId: String) {
            TODO("Not yet implemented")
        }

        override fun addToBlacklist(traderId: String) {
            TODO("Not yet implemented")
        }

    }

    override val listPresenter = GeneralFeedRVListPresenter()

    fun openSignInScreen() {
        router.navigateTo(Screens.signInScreen(false))
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.signUpScreen(false))
    }
}
