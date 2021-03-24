package ru.wintrade.mvp.presenter

import androidx.fragment.app.Fragment
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.IMainTradersVPListPresenter
import ru.wintrade.mvp.view.MainTradersView
import ru.wintrade.ui.fragment.AllTradersFragment
import javax.inject.Inject

class MainTradersPresenter : MvpPresenter<MainTradersView>() {
    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    val listPresenter = MainTradersVPListPresenter()

    inner class MainTradersVPListPresenter : IMainTradersVPListPresenter {
        private val mainTradersVPListOfFragment = listOf(
            AllTradersFragment.newInstance(),
            AllTradersFragment.newInstance()
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