package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.LoadingView
import ru.wintrade.navigation.Screens
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoadingPresenter : MvpPresenter<LoadingView>() {

    @Inject
    lateinit var router: Router

    private val loadingTime = 3L

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        getTimer().subscribe(
            {
                router.newRootScreen(Screens.OnBoardScreen())
            },
            {}
        )
    }

    private fun getTimer() =
        Single.timer(loadingTime, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())

}