package ru.fabulus.fabulustrade.mvp.presenter.generalfeed

import io.reactivex.rxjava3.core.Single
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
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

    fun openSignInScreen() {
        router.navigateTo(Screens.signInScreen(false))
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.signUpScreen(false))
    }
}
