package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.Traders
import ru.wintrade.mvp.model.datasource.DataSource
import ru.wintrade.mvp.model.retrofit.RetrofitImpl
import ru.wintrade.mvp.presenter.adapter.IAllTradersListPresenter
import ru.wintrade.mvp.view.AllTradersView
import ru.wintrade.mvp.view.item.AllTradersItemView
import javax.inject.Inject

class AllTradersPresenter(private val dataSource: DataSource<List<Traders>> = RetrofitImpl()) :
    MvpPresenter<AllTradersView>() {
    @Inject
    lateinit var router: Router

    val listPresenter = AllTradersListPresenter()

    inner class AllTradersListPresenter : IAllTradersListPresenter {
        var traderList = listOf<Traders>()
        override fun getCount(): Int = traderList.size

        override fun bind(view: AllTradersItemView) {
            view.setTraderFio(traderList[view.pos].fio)
            view.setTraderFollowerCount(traderList[view.pos].fCount)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        getTraderList()
    }

    private fun getTraderList() {
        val single: Observable<List<Traders>> = dataSource.getDataFromDataSource()
        val disposable: Disposable = single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.renderData(it)
            }, {
                it.printStackTrace()
            })
    }
}