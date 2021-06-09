package ru.wintrade.mvp.presenter.subscriber

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Subscription
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.IObservationListPresenter
import ru.wintrade.mvp.view.item.ObservationItemView
import ru.wintrade.mvp.view.subscriber.SubscriberObservationView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class SubscriberObservationPresenter : MvpPresenter<SubscriberObservationView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    var listPresenter = SubscriberObservationListPresenter()

    inner class SubscriberObservationListPresenter : IObservationListPresenter {
        var traders = mutableListOf<Subscription>()
        override fun getCount(): Int = traders.size

        override fun bind(view: ObservationItemView) {
            val traderList = traders[view.pos]
            traderList.trader.username?.let { view.setTraderName(it) }
            traderList.trader.avatar?.let { view.setTraderAvatar(it) }
            traderList.trader.yearProfit?.let { view.setTraderProfit(it) }
            traderList.status?.let {
                if (it.toInt() == 1)
                    view.subscribeStatus("Наблюдение")
                else
                    view.subscribeStatus("Подписка")
            }
        }

        override fun onItemClick(pos: Int) {
            val trader = traders[pos]
            if (trader.trader.id == profile.user!!.id)
                router.navigateTo(Screens.TraderMeMainScreen())
            else
                router.navigateTo(Screens.TraderMainScreen(trader.trader))
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
                    val traders = subscriptions.sortedBy { it.status }.reversed()
                    listPresenter.traders.addAll(traders)
                    viewState.updateAdapter()
                }, {
                    it.printStackTrace()
                }
            )
    }
}