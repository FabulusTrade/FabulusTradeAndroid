package ru.fabulus.fabulustrade.mvp.presenter.generalfeed

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostRVListPresenter
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostWithBlacklistRVListPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.fabulus.fabulustrade.mvp.view.item.PostItemView
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
            viewState.showMessageSureToAddToBlacklist(traderId)
        }

        override fun bind(view: PostItemView) {
            super.bind(view)
            view.initFlashFooterVisibility(false)
        }

        override fun addToBlacklist(traderId: String) {
            apiRepo.addToBlacklist(profile.token!!, traderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseAddToBlackList ->
                    reloadPosts()
                    viewState.showMessagePostAddedToBlacklist()
                }, {
                    it.printStackTrace()
                })
        }

        override fun showCommentDetails(view: PostItemView) {
            router.navigateTo(Screens.postDetailFragment(postList[view.pos], true))
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
