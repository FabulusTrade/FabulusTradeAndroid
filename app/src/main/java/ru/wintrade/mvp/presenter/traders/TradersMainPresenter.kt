package ru.wintrade.mvp.presenter.traders

import androidx.fragment.app.Fragment
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITradersMainVPListPresenter
import ru.wintrade.mvp.view.traders.TradersMainView
import ru.wintrade.ui.fragment.traders.TradersAllFragment
import ru.wintrade.ui.fragment.traders.TradersFilterFragment
import javax.inject.Inject

class TradersMainPresenter : MvpPresenter<TradersMainView>() {
    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileStorage: ProfileStorage

    val listPresenter = TradersMainVPListPresenter()

    inner class TradersMainVPListPresenter : ITradersMainVPListPresenter {
        private val mainTradersVPListOfFragment = listOf<Fragment>(
            TradersAllFragment.newInstance(),
            TradersFilterFragment.newInstance()
        )

        override fun getCount(): Int = mainTradersVPListOfFragment.size
        override fun getFragmentList(): List<Fragment> {
            return mainTradersVPListOfFragment
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}