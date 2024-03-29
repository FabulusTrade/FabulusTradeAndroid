package ru.fabulus.fabulustrade.mvp.presenter.traderme

import android.os.Handler
import android.os.Looper
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.Subscription
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IObservationListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.ObservationItemView
import ru.fabulus.fabulustrade.mvp.view.traderme.TraderMeObservationView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.stringColorToIntWithDef
import javax.inject.Inject

class TraderMeObservationPresenter : MvpPresenter<TraderMeObservationView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    var listPresenter = TraderMeObservationListPresenter()
    var subscriptionClickPos = -1

    inner class TraderMeObservationListPresenter : IObservationListPresenter {
        var traders = mutableListOf<Subscription>()
        override fun getCount(): Int = traders.size

        override fun bind(view: ObservationItemView) {
            val traderList = traders[view.pos]
            val trader = traderList.trader
            trader.username?.let { username -> view.setTraderName(username) }
            trader.avatar?.let { avatar -> view.setTraderAvatar(avatar) }

            view.setTraderProfit(
                resourceProvider.formatDigitWithDef(
                    R.string.trader_me_tv_subscriber_observation_profit_text,
                    trader.colorIncrDecrDepo365?.value
                ),
                resourceProvider.stringColorToIntWithDef(trader.colorIncrDecrDepo365?.color)
            )

            traderList.status?.let { status ->
                view.subscribeStatus(status.toInt() != 1)
            }
        }

        override fun onItemClick(pos: Int) {
            val trader = traders[pos]
            if (trader.trader.id == profile.user!!.id)
                router.navigateTo(Screens.traderMeMainScreen())
            else
                router.navigateTo(Screens.traderMainScreen(trader.trader))
        }

        override fun deleteObservation(pos: Int) {
            if (profile.user == null) {
                router.navigateTo(Screens.signInScreen(false))
            } else {
                apiRepo
                    .deleteObservation(profile.token!!, traders[pos].trader.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {})

                reLoadSubscriptions()
            }
        }

        override fun deleteSubscription(pos: Int) {
            if (profile.user == null) {
                router.navigateTo(Screens.signInScreen(false))
            } else {
                if (needDeleteSubscription(pos)) {
                    apiRepo
                        .deleteObservation(profile.token!!, traders[pos].trader.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({}, {})
                    reLoadSubscriptions()
                } else {
                    viewState.showToast(resourceProvider.getStringResource(R.string.unsubscribe_from_trader_alarm_message))
                }
            }
        }

        private fun needDeleteSubscription(pos: Int): Boolean {
            return if (subscriptionClickPos == pos) {
                clearSubscriptionClickPos()
                true
            } else {
                subscriptionClickPos = pos
                false
            }
        }

        private fun reLoadSubscriptions(delayMills: Long = 200) {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    loadSubscriptions()
                }, delayMills
            )
        }
    }

    override fun attachView(view: TraderMeObservationView?) {
        super.attachView(view)
        clearSubscriptionClickPos()
    }

    private fun clearSubscriptionClickPos() {
        subscriptionClickPos = -1
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        apiRepo
            .mySubscriptions(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { subscriptions ->
                    val traders = subscriptions.sortedBy { it.status }.reversed()
                    listPresenter.traders.clear()
                    listPresenter.traders.addAll(traders)
                    viewState.updateAdapter()
                }, {
                    it.printStackTrace()
                }
            )
    }

    fun openTraderMeSubScreen(position: Int) {
        router.navigateTo(Screens.traderMeSubTradeScreen(position))
    }
}