package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_trader_stat.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderStatPresenter
import ru.wintrade.mvp.view.trader.TraderStatView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.ui.adapter.TraderVPAdapter
import java.lang.IllegalStateException

class TraderStatFragment : MvpAppCompatFragment(), TraderStatView, BackButtonListener {
    companion object {
        fun newInstance() = TraderStatFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderStatPresenter

    @ProvidePresenter
    fun providePresenter() = TraderStatPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_stat, container, false)

    override fun init() {
        vp_trader_stat.adapter = TraderVPAdapter(this, presenter.listPresenter)
        TabLayoutMediator(
            tab_layout_trader_stat,
            vp_trader_stat
        ) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_trader_profit)
                1 -> tab.setIcon(R.drawable.ic_trader_news)
                2 -> tab.setIcon(R.drawable.ic_trader_instrument)
                3 -> tab.setIcon(R.drawable.ic_trader_deal)
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    override fun backClicked(): Boolean {
        presenter.backClicked()
        return true
    }
}