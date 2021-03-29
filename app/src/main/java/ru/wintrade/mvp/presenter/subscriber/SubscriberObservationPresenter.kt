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
import javax.inject.Inject

class SubscriberObservationPresenter : MvpPresenter<SubscriberObservationView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileStorage: ProfileStorage

    @Inject
    lateinit var apiRepo: ApiRepo

    var traders = mutableListOf<Trader>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        apiRepo.mySubscriptions(profileStorage.profile!!.token).subscribe(
            { listSubs ->
                apiRepo.getAllTradersSingle().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { allTraders ->
                        allTraders.forEach { trader ->
                            listSubs.forEach{ sub ->
                                if (trader.username == sub.trader)
                                    traders.add(trader)
                            }
                        }

                        listPresenter.subscriberObservationList.clear()
                        listPresenter.subscriberObservationList.addAll(traders)
                        viewState.updateAdapter()
                    },
                    {
                        it.printStackTrace()
                    }
                )
            },
            {
                it.printStackTrace()
            }
        )
    }

    var listPresenter = SubscriberObservationListPresenter()

    inner class SubscriberObservationListPresenter: ISubscriberObservationListPresenter {
        var subscriberObservationList = mutableListOf<Trader>()
        override fun getCount(): Int = subscriberObservationList.size

        override fun bind(view: SubscriberObservationItemView) {
            val trader = subscriberObservationList[view.pos]
            view.setTraderName(trader.username)
            view.setTraderAvatar(trader.avatar)
            view.setTraderProfit(trader.yearProfit)
        }

    }
}