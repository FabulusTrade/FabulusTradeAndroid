package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderDealPresenter
import ru.wintrade.mvp.view.trader.TraderDealView
import ru.wintrade.ui.App

class TraderTradeFragment : MvpAppCompatFragment(), TraderDealView {
    companion object {
        fun newInstance() = TraderTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderDealPresenter

    @ProvidePresenter
    fun providePresenter() = TraderDealPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_deal, container, false)

    override fun init() {

    }
}