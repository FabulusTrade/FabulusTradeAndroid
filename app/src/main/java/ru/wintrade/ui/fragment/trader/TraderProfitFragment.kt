package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trader_profit.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter
import ru.wintrade.mvp.view.trader.TraderProfitView
import ru.wintrade.ui.App

class TraderProfitFragment(val trader: Trader? = null) : MvpAppCompatFragment(), TraderProfitView {
    companion object {
        fun newInstance(trader: Trader) = TraderProfitFragment(trader)
    }

    @InjectPresenter
    lateinit var presenter: TraderProfitPresenter

    @ProvidePresenter
    fun providePresenter() = TraderProfitPresenter(trader!!).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_profit, container, false)

    override fun init() {
        with(bar_chart_trader_profit) {
            data = presenter.setupBarChart()
            legend.isEnabled = false
            data.setDrawValues(false)
            animateY(3000)
            setDescription("")
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawZeroLine(true)
            xAxis.isEnabled = false
            axisRight.isEnabled = false
        }
    }

    override fun setDateJoined(date: String) {
        tv_trader_profit_registration_date_value.text = date
    }

    override fun setFollowersCount(followersCount: Int) {
        tv_trader_profit_follower_counter.text = followersCount.toString()
    }

    override fun setTradesCount(tradesCount: Int) {
        tv_trader_profit_deal_for_week_count.text = tradesCount.toString()
    }
}