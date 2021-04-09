package ru.wintrade.mvp.presenter.trader

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITraderStatVPListPresenter
import ru.wintrade.mvp.view.trader.TraderStatView
import ru.wintrade.navigation.Screens
import ru.wintrade.ui.fragment.trader.TraderDealFragment
import ru.wintrade.ui.fragment.trader.TraderNewsFragment
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.trader.TraderProfitFragment
import javax.inject.Inject

class TraderStatPresenter(val trader: Trader) : MvpPresenter<TraderStatView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profileStorage: ProfileStorage

    var isObserveActive: Boolean = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        checkSubscription()
    }

    val listPresenter = TraderVPListPresenter()

    inner class TraderVPListPresenter : ITraderStatVPListPresenter {
        private val viewPagerListOfFragment = mutableListOf<Fragment>(
            TraderProfitFragment.newInstance(trader),
            TraderNewsFragment.newInstance(),
            TraderPopularInstrumentsFragment.newInstance(),
            TraderDealFragment.newInstance()
        )

        override fun getCount(): Int = viewPagerListOfFragment.size

        override fun getFragmentList(): List<Fragment> {
            return viewPagerListOfFragment
        }
    }

    fun observeBtnClicked() {
        isObserveActive = !isObserveActive
        viewState.setObserveActive(isObserveActive)
    }

    fun subscribeToTraderBtnClicked() {
        apiRepo.subscribeToTrader(profileStorage.profile!!.token, trader.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setSubscribeBtnActive(false)
                viewState.setObserveVisibility(false)
            }, {
                it.printStackTrace()
            })
    }

    private fun checkSubscription() {
        apiRepo.mySubscriptions(profileStorage.profile!!.token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listSubs ->
                listSubs.find { it.id == trader.id }?.let {
                    setVisibility(false)
                } ?: setVisibility(true)
            }, {
                it.printStackTrace()
            })
    }

    private fun setVisibility(result: Boolean) {
        viewState.setSubscribeBtnActive(result)
        viewState.setObserveVisibility(result)
    }

    fun backClicked(): Boolean {
        router.backTo(Screens.TradersMainScreen())
        return true
    }
}