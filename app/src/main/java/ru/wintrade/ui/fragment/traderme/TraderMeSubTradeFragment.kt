package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeSubTradePresenter
import ru.wintrade.mvp.view.traderme.TraderMeSubTradeView
import ru.wintrade.ui.App

class TraderMeSubTradeFragment : MvpAppCompatFragment(), TraderMeSubTradeView {
    companion object {
        fun newInstance() = TraderMeSubTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMeSubTradePresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeSubTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_subs_trade, container, false)

    override fun init() {

    }

}