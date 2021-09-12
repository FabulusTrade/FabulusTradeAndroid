package ru.wintrade.mvp.presenter.traderme

import android.os.Handler
import android.os.Looper
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Subscription
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.IObservationListPresenter
import ru.wintrade.mvp.view.item.ObservationItemView
import ru.wintrade.mvp.view.traderme.TraderMeObservationView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.doubleToStringWithFormat
import javax.inject.Inject

class TraderMeObservationPresenter : MvpPresenter<TraderMeObservationView>() {

    companion object {
        private const val PROFIT_FORMAT = "0.00"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    var listPresenter = TraderMeObservationListPresenter()

    inner class TraderMeObservationListPresenter : IObservationListPresenter {
        var traders = mutableListOf<Subscription>()
        override fun getCount(): Int = traders.size

        override fun bind(view: ObservationItemView) {
            val traderList = traders[view.pos]
            traderList.trader.username?.let { view.setTraderName(it) }
            traderList.trader.avatar?.let { view.setTraderAvatar(it) }
            traderList.trader.yearProfit?.let {
                view.setTraderProfit(
                    it.doubleToStringWithFormat(
                        PROFIT_FORMAT
                    )
                )
            }
            traderList.status?.let {
                if (it.toInt() == 1)
                    view.subscribeStatus(false)
                else
                    view.subscribeStatus(true)
            }
        }

        override fun onItemClick(pos: Int) {
            val trader = traders[pos]
            if (trader.trader.id == profile.user!!.id)
                router.navigateTo(Screens.TraderMeMainScreen())
            else
                router.navigateTo(Screens.TraderMainScreen(trader.trader))
        }

        override fun deleteObservation(pos: Int) {
            if (profile.user == null) {
                router.navigateTo(Screens.SignInScreen())
            } else {
                apiRepo.deleteObservation(profile.token!!, traders[pos].trader.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {})
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        loadSubscriptions()
                    }, 200
                )
            }
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
                    listPresenter.traders.clear()
                    listPresenter.traders.addAll(traders)
                    viewState.updateAdapter()
                }, {
                    it.printStackTrace()
                }
            )
    }

    fun openTraderMeSubScreen(position: Int) {
        router.navigateTo(Screens.TraderMeSubTradeScreen(position))
    }
}