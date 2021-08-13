package ru.wintrade.mvp.presenter

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.TradesSortedByCompany
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ICompanyTradingOperationsListPresenter
import ru.wintrade.mvp.view.CompanyTradingOperationsView
import ru.wintrade.mvp.view.item.CompanyTradingOperationsItemView
import ru.wintrade.navigation.Screens
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CompanyTradingOperationsPresenter(val traderId: String, val companyId: Int) :
    MvpPresenter<CompanyTradingOperationsView>() {
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
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        override fun getCount(): Int = dealsList.size

        override fun bind(view: CompanyTradingOperationsItemView) {
            val deals = dealsList[view.pos]
            view.setCompanyLogo(deals.companyImg)
            view.setOperationDate("Дата ${dateFormat.format(deals.date)}")
            view.setOperationType(deals.operationType)
            view.setTradePrice(deals.price.toString() + deals.currency)
            if (deals.profitCount.isNullOrEmpty()) {
                view.setProfitCount(null)
            } else {
                view.setProfitCount("${deals.profitCount}%")
            }
        }

        override fun itemClicked(view: CompanyTradingOperationsItemView) {
            profile.token?.let { token ->
                apiRepo.getTradeById(token, dealsList[view.pos].id)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ trade ->
                        router.navigateTo(Screens.TradeDetailScreen(trade))
                    }, {})
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
            apiRepo.getTraderTradesByCompany(it, traderId, companyId, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { pag ->
                        listPresenter.dealsList.addAll(pag.results)
                        viewState.updateRecyclerView()
                        viewState.setCompanyName(pag.results[0].company)
                        nextPage = pag.next
                    }, {}
                )
        }
    }

    fun onScrollLimit() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            profile.token?.let {
                apiRepo.getTraderTradesByCompany(it, traderId, companyId, nextPage!!)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ pag ->
                        listPresenter.dealsList.addAll(pag.results)
                        viewState.updateRecyclerView()
                        nextPage = pag.next
                        isLoading = false
                    }, {
                        it.printStackTrace()
                        isLoading = false
                    })
            }
        }
    }
}