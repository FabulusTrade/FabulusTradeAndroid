package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.IAllTradersListPresenter
import ru.wintrade.mvp.view.AllTradersView
import ru.wintrade.mvp.view.item.AllTradersItemView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class AllTradersPresenter : MvpPresenter<AllTradersView>() {

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    val listPresenter = AllTradersListPresenter()

    inner class AllTradersListPresenter : IAllTradersListPresenter {
        var traderList = mutableListOf<Trader>()
        override fun getCount(): Int = traderList.size

        override fun bind(view: AllTradersItemView) {
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

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}