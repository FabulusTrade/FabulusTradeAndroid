package ru.fabulus.fabulustrade.mvp.presenter

import android.os.Handler
import android.os.Looper
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.BlacklistItem
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IBlacklistListPresenter
import ru.fabulus.fabulustrade.mvp.view.BlacklistView
import ru.fabulus.fabulustrade.mvp.view.item.BlacklistItemView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.stringColorToIntWithDef
import javax.inject.Inject

class BlacklistPresenter : MvpPresenter<BlacklistView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    var listPresenter = BlacklistListPresenter()
    var subscriptionClickPos = -1

    inner class BlacklistListPresenter : IBlacklistListPresenter {
        var traders = mutableListOf<BlacklistItem>()
        override fun getCount(): Int = traders.size

        override fun bind(view: BlacklistItemView) {
            val traderList = traders[view.pos]
            val trader = traderList.trader
            trader.username?.let { username -> view.setTraderName(username) }
            trader.avatar?.let { avatar -> view.setTraderAvatar(avatar) }
            view.setTraderProfit(
                resourceProvider.formatDigitWithDef(
                    R.string.tv_subscriber_observation_profit_text,
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

        private fun reLoadSubscriptions(delayMills: Long = 200) {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    loadBlacklist()
                }, delayMills
            )
        }
    }

    override fun attachView(view: BlacklistView?) {
        super.attachView(view)
        loadBlacklist()
        clearSubscriptionClickPos()
    }

    private fun clearSubscriptionClickPos() {
        subscriptionClickPos = - 1
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    private fun loadBlacklist() {
        apiRepo
            .getBlacklist(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ subscriptions ->
                val traders = subscriptions.sortedBy { it.status }.reversed()
                listPresenter.traders.clear()
                listPresenter.traders.addAll(traders)
                if (traders.isEmpty()) {
                    viewState.withoutSubscribeAnyTrader()
                    return@subscribe
                }
                viewState.updateAdapter()
            }, {
                it.printStackTrace()
            })
    }
}