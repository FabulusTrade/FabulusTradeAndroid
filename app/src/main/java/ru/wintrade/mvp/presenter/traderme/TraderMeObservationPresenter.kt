package ru.wintrade.mvp.presenter.traderme

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.IObservationListPresenter
import ru.wintrade.mvp.view.item.ObservationItemView
import ru.wintrade.mvp.view.traderme.TraderMeObservationView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TraderMeObservationPresenter : MvpPresenter<TraderMeObservationView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    var listPresenter = TraderMeObservationListPresenter()

    inner class TraderMeObservationListPresenter : IObservationListPresenter {
        var traders = mutableListOf<Trader>()
        override fun getCount(): Int = traders.size

        override fun bind(view: ObservationItemView) {
            val traderList = traders[view.pos]
            traderList.username?.let { view.setTraderName(it) }
            traderList.avatar?.let { view.setTraderAvatar(it) }
            traderList.yearProfit?.let { view.setTraderProfit(it) }
        }

        override fun onItemClick(pos: Int) {
            val trader = traders[pos]
            if (trader.id == profile.user!!.id)
                router.navigateTo(Screens.TraderMeMainScreen())
            else
                router.navigateTo(Screens.TraderMainScreen(trader))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        apiRepo.mySubscriptions(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { subscriptions ->
                    val traders = subscriptions.map { it.trader }
                    listPresenter.traders.addAll(traders)
                    viewState.updateAdapter()
                }, {
                    it.printStackTrace()
                }
            )
    }
}