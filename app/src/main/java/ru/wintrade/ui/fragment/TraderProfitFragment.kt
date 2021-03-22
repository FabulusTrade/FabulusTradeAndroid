package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trader_profit.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.TraderProfitPresenter
import ru.wintrade.mvp.view.TraderProfitView
import ru.wintrade.ui.App

class TraderProfitFragment : MvpAppCompatFragment(), TraderProfitView {
    companion object {
        fun newInstance() = TraderProfitFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderProfitPresenter

    @ProvidePresenter
    fun providePresenter() = TraderProfitPresenter().apply {
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
}