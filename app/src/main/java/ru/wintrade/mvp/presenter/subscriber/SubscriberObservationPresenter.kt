package ru.wintrade.mvp.presenter.subscriber

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ISubscriberObservationListPresenter
import ru.wintrade.mvp.view.item.SubscriberObservationItemView
import ru.wintrade.mvp.view.subscriber.SubscriberObservationView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class SubscriberObservationPresenter : MvpPresenter<SubscriberObservationView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileStorage: ProfileStorage

    @Inject
    lateinit var apiRepo: ApiRepo

    var listPresenter = SubscriberObservationListPresenter()

    inner class SubscriberObservationListPresenter : ISubscriberObservationListPresenter {
        var traders = mutableListOf<Trader>()
        override fun getCount(): Int = traders.size

        override fun bind(view: SubscriberObservationItemView) {
            val traderList = traders[view.pos]
            traderList.username?.let { view.setTraderName(it) }
            traderList.avatar?.let { view.setTraderAvatar(it) }
            traderList.yearProfit?.let { view.setTraderProfit(it) }
        }

        override fun openTraderStat(pos: Int) {
            router.navigateTo(Screens.TraderStatScreen(traders[pos]))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        apiRepo.mySubscriptions(profileStorage.profile!!.token)
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