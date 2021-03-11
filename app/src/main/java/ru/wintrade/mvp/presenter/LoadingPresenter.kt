package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.presenter.adapter.ILoadingListPresenter
import ru.wintrade.mvp.view.LoadingView
import ru.wintrade.mvp.view.item.LoadingItemView
import ru.wintrade.navigation.Screens
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoadingPresenter : MvpPresenter<LoadingView>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var router: Router

    val loadingListPresenter = LoadingListPresenter()
    private val loadingTime = 3L

    inner class LoadingListPresenter : ILoadingListPresenter {
        val images = mutableListOf<Int>()

        override fun getCount() = images.size

        override fun bindView(view: LoadingItemView) {
            view.setImage(images[view.pos])
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        val images = resourceProvider.getLoadingImages()
        viewState.setupTabs(images.size)
        loadingListPresenter.images.clear()
        loadingListPresenter.images.addAll(images)

        getTimer().subscribe(
            {
                router.replaceScreen(Screens.OnBoardScreen())
            },
            {}
        )
    }

    fun pageChanged(pos: Int) {
        viewState.tabChanged(pos)
    }

    private fun getTimer() = Single.timer(loadingTime, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())

}