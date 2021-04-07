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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        isAlreadySubscribe()
        viewState.init()
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

    fun subscribeToTrader() {
        profileStorage.profile?.let {
            apiRepo.subscribeToTrader(it.token, trader.id).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.subscribeToTrader()
                    isAlreadySubscribe()
                }, {

                    }
                )
        }
    }

    fun isAlreadySubscribe() {
        profileStorage.profile?.let { profile ->
            apiRepo.mySubscriptions(profile.token).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listSubs ->
                    val listTrader = mutableListOf<Trader>()
                    if (listSubs.isEmpty()) {
                        viewState.setButtonVisibility(true)
                    } else
                        listSubs.forEach {
                            if (it.id == trader.id) {
                                listTrader.add(trader)
                            }
                        }
                    if (listTrader.isEmpty()) {
                        viewState.setButtonVisibility(true)
                    } else {
                        viewState.setButtonVisibility(false)
                    }
                }, {

                })
        }
    }

    fun backClicked(): Boolean {
        router.backTo(Screens.TradersMainScreen())
        return true
    }
}