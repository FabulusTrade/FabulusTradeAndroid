package ru.fabulus.fabulustrade.mvp.presenter

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.TradesSortedByCompany
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ICompanyTradingOperationsListPresenter
import ru.fabulus.fabulustrade.mvp.view.CompanyTradingOperationsView
import ru.fabulus.fabulustrade.mvp.view.item.CompanyTradingOperationsItemView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.doubleToStringWithFormat
import ru.fabulus.fabulustrade.util.toStringFormat
import javax.inject.Inject

class CompanyTradingOperationsPresenter(
    val traderId: String,
    val companyId: Int
) : MvpPresenter<CompanyTradingOperationsView>() {

    companion object {
        private const val PROFIT_FORMAT = "0.00"
        private const val ZERO = 0.0
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    private var isLoading = false
    private var nextPage: Int? = 1

    val listPresenter = CompanyTradingOperationsRvListPresenter()

    inner class CompanyTradingOperationsRvListPresenter : ICompanyTradingOperationsListPresenter {
        val dealsList = mutableListOf<TradesSortedByCompany>()
        override fun getCount(): Int = dealsList.size

        override fun bind(view: CompanyTradingOperationsItemView) {
            val deals = dealsList[view.pos]
            view.setCompanyLogo(deals.companyImg)
            view.setOperationDate("Дата ${deals.date.toStringFormat()}")
            view.setOperationType(deals.operationType)
            view.setTradePrice(deals.price.toString() + deals.currency)
            view.setClipVisibility(deals.posts != null)
            if (deals.profitCount == null) {
                view.setProfitCount(null)
            } else {
                view.setProfitCount(
                    deals.profitCount.doubleToStringWithFormat(
                        PROFIT_FORMAT,
                        true
                    )
                )
            }
        }

        override fun itemClicked(view: CompanyTradingOperationsItemView) {
            profile.token?.let { token ->
                apiRepo
                    .getTradeById(token, dealsList[view.pos].id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ trade ->
                        if (trade.posts == null) {
                            router.navigateTo(Screens.tradeDetailScreen(trade, false))
                        } else {
                            router.navigateTo(Screens.tradeArgumentScreen(trade))
                        }
                    }, {
                        // Ошибка не обрабатывается
                    })
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadCompanyDeals()
    }

    private fun loadCompanyDeals() {
        profile.token?.let {
            apiRepo
                .getTraderTradesByCompany(it, traderId, companyId, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.dealsList.addAll(pag.results)
                    viewState.updateRecyclerView()
                    viewState.setCompanyName(pag.results[0].company)
                    nextPage = pag.next
                }, {
                    // Ошибка не обрабатывается
                })
        }
    }

    fun openJournal() {
        router.navigateTo(Screens.companyTradingOperationsJournalScreen(traderId, companyId))
    }

    fun onScrollLimit() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            profile.token?.let {
                apiRepo
                    .getTraderTradesByCompany(it, traderId, companyId, nextPage!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ pag ->
                        listPresenter.dealsList.addAll(pag.results)
                        viewState.updateRecyclerView()
                        nextPage = pag.next
                        isLoading = false
                    }, { t ->
                        t.printStackTrace()
                        isLoading = false
                    })
            }
        }
    }
}