package ru.wintrade.mvp.presenter.subscriber

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ISubscriberMainVPListPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberMainView
import ru.wintrade.ui.fragment.subscriber.SubscriberDealFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberNewsFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberObservationFragment
import javax.inject.Inject

class SubscriberMainPresenter : MvpPresenter<SubscriberMainView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profileStorage: ProfileStorage

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setAvatar(profileStorage.profile?.avatar)
        profileStorage.profile?.let { viewState.setName(it.username) }
        getSubscriptionCount()
    }

    fun getSubscriptionCount() {
        apiRepo.getProfile(profileStorage.profile!!.token).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setSubscriptionCount(it.subscriptions_count)
            }, {
                it.printStackTrace()
            })
    }

    val listPresenter = SubscriberMainVPListPresenter()

    inner class SubscriberMainVPListPresenter : ISubscriberMainVPListPresenter {
        private val viewPagerListOfFragment = listOf<Fragment>(
            SubscriberObservationFragment.newInstance(),
            SubscriberDealFragment.newInstance(),
            SubscriberNewsFragment.newInstance()
        )

        override fun getCount(): Int = viewPagerListOfFragment.size

        override fun getFragmentList(): List<Fragment> {
            return viewPagerListOfFragment
        }
    }
}