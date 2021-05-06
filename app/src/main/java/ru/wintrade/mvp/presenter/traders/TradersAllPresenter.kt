package ru.wintrade.mvp.presenter.traders

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITradersAllListPresenter
import ru.wintrade.mvp.view.traders.TradersAllView
import ru.wintrade.mvp.view.item.TradersAllItemView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TradersAllPresenter : MvpPresenter<TradersAllView>() {
    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileStorage: ProfileStorage

    val listPresenter = TradersAllListPresenter()
    private var isLoading = false
    private var nextPage: Int? = 1

    inner class TradersAllListPresenter : ITradersAllListPresenter {
        var traderList = mutableListOf<Trader>()
        override fun getCount(): Int = traderList.size

        override fun bind(view: TradersAllItemView) {
            traderList[view.pos].username?.let { view.setTraderName(it) }
            traderList[view.pos].yearProfit?.let { view.setTraderProfit(it) }
            traderList[view.pos].avatar?.let { view.setTraderAvatar(it) }
        }

        override fun openTraderStat(pos: Int) {
            router.navigateTo(Screens.TraderForSubscriberMainScreen(traderList[pos]))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadTraders()
    }

    fun onScrollLimit() {
        loadTraders()
    }

    private fun loadTraders() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            apiRepo.getAllTraders(nextPage!!).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { pag ->
                        listPresenter.traderList.addAll(pag.results)
                        viewState.updateAdapter()
                        nextPage = pag.next
                        isLoading = false
                    },
                    {
                        it.printStackTrace()
                        isLoading = false
                    }
                )
        }
    }
}