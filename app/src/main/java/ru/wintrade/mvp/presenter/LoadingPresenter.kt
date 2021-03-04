package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.presenter.adapter.ILoadingListPresenter
import ru.wintrade.mvp.view.LoadingView
import ru.wintrade.mvp.view.item.LoadingItemView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.ResourceHelper
import javax.inject.Inject

class LoadingPresenter : MvpPresenter<LoadingView>() {

    @Inject
    lateinit var resourceHelper: ResourceHelper

    @Inject
    lateinit var router: Router

    val loadingListPresenter = LoadingListPresenter()

    inner class LoadingListPresenter : ILoadingListPresenter {
        val images = mutableListOf<Int>()

        override fun onSkipBtnClick() {
            skipSplash()
        }

        override fun getCount() = images.size

        override fun bindView(view: LoadingItemView) {
            view.setImage(images[view.pos])
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        val images = resourceHelper.getLoadingImages()
        viewState.setupTabs(images.size)
        loadingListPresenter.images.clear()
        loadingListPresenter.images.addAll(images)
    }

    fun pageChanged(pos: Int) {
        viewState.tabChanged(pos)
    }

    private fun skipSplash() {
        router.navigateTo(Screens.OnBoardScreen())
    }
}