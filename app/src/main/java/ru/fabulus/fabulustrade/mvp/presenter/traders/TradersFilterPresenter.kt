package ru.fabulus.fabulustrade.mvp.presenter.traders

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.view.traders.TradersFilterView
import ru.fabulus.fabulustrade.navigation.Screens
import javax.inject.Inject

class TradersFilterPresenter(val filter: Int) : MvpPresenter<TradersFilterView>() {
    @Inject
    lateinit var router: Router

    enum class CheckedFilter {
        BY_PROFIT, BY_FOLLOWERS
    }

    lateinit var checkedFilter: CheckedFilter

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        when (filter) {
            CheckedFilter.BY_PROFIT.ordinal -> checkedFilter = CheckedFilter.BY_PROFIT
            CheckedFilter.BY_FOLLOWERS.ordinal -> checkedFilter = CheckedFilter.BY_FOLLOWERS
        }
        viewState.setFilterCheckBoxState(checkedFilter)
    }

    fun byProfitBoxClicked() {
        checkedFilter = CheckedFilter.BY_PROFIT
        viewState.setFilterCheckBoxState(checkedFilter)
    }

    fun byFollowersBoxClicked() {
        checkedFilter = CheckedFilter.BY_FOLLOWERS
        viewState.setFilterCheckBoxState(checkedFilter)
    }

    fun applyFilter() {
        router.navigateTo(Screens.tradersMainScreen(checkedFilter.ordinal))
    }
}