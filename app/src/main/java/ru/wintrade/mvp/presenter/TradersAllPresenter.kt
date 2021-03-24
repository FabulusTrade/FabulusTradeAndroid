package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITradersAllListPresenter
import ru.wintrade.mvp.view.TradersAllView
import ru.wintrade.mvp.view.item.TradersAllItemView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TradersAllPresenter : MvpPresenter<TradersAllView>() {
    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    val listPresenter = TradersAllListPresenter()

    inner class TradersAllListPresenter : ITradersAllListPresenter {
        var traderList = mutableListOf<Trader>()
        override fun getCount(): Int = traderList.size

        override fun bind(view: TradersAllItemView) {
            view.setTraderFio(traderList[view.pos].fio)
            view.setTraderFollowerCount(traderList[view.pos].followersCount)
        }

        override fun openTraderStat(pos: Int) {
            router.navigateTo(Screens.TraderStatScreen())
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        getTraderList()
    }

    private fun getTraderList() {
        apiRepo.getAllTradersSingle().observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                listPresenter.traderList.clear()
                listPresenter.traderList.addAll(it)
                viewState.updateRecyclerView()
            },
            {
                it.printStackTrace()
            }
        )
    }
}