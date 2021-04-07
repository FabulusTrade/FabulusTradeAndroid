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

    var traders = mutableListOf<Trader>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        profileStorage.profile?.let { profile ->
            apiRepo.mySubscriptions(profile.token)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        listPresenter.subscriberObservationList.clear()
                        listPresenter.subscriberObservationList.addAll(it)
                        viewState.updateAdapter()
                    }, {
                        it.printStackTrace()
                    }
                )
        }
    }

    var listPresenter = SubscriberObservationListPresenter()

    inner class SubscriberObservationListPresenter : ISubscriberObservationListPresenter {
        var subscriberObservationList = mutableListOf<Trader>()
        override fun getCount(): Int = subscriberObservationList.size

        override fun bind(view: SubscriberObservationItemView) {
            val traderList = subscriberObservationList[view.pos]
            traderList.username?.let { view.setTraderName(it) }
            traderList.avatar?.let { view.setTraderAvatar(it) }
            traderList.yearProfit?.let { view.setTraderProfit(it) }
        }

        override fun openTraderStat(pos: Int) {
            router.navigateTo(Screens.TraderStatScreen(subscriberObservationList[pos]))
        }
    }
}