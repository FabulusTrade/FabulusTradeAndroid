package ru.wintrade.mvp.presenter

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.view.WebViewView
import javax.inject.Inject

class WebViewPresenter(val url: String) : MvpPresenter<WebViewView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setMainContent(url)
    }

    fun onCLoseClicked() {
        router.exit()
    }

}
