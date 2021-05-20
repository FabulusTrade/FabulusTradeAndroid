package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trader_me_profit.*
import kotlinx.android.synthetic.main.layout_attached_post.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeProfitPresenter
import ru.wintrade.mvp.view.traderme.TraderMeProfitView
import ru.wintrade.ui.App

class TraderMeProfitFragment : MvpAppCompatFragment(),
    TraderMeProfitView {
    companion object {
        fun newInstance() = TraderMeProfitFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMeProfitPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeProfitPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_profit, container, false)

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun init() {
        with(bar_chart_trader_me_profit) {
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

        layout_trader_me_profit_attached_post.setOnClickListener {
            presenter.pinnedPostClicked()
        }
    }

    override fun setDateJoined(date: String) {
        tv_trader_me_profit_registration_date_value.text = date
    }

    override fun setFollowersCount(followersCount: Int) {
        tv_trader_me_profit_follower_counter.text = followersCount.toString()
    }

    override fun setTradesCount(tradesCount: Int) {
        tv_trader_me_profit_deal_for_week_count.text = tradesCount.toString()
    }

    override fun setPinnedPostText(text: String) {
        tv_attachet_post.text = text
    }
}