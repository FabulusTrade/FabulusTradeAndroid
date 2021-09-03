package ru.wintrade.ui.fragment.trader

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trader_profit.*
import kotlinx.android.synthetic.main.layout_attached_post.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter
import ru.wintrade.mvp.view.trader.TraderProfitView
import ru.wintrade.ui.App

class TraderProfitFragment : MvpAppCompatFragment(), TraderProfitView {

    companion object {
        private const val MAX_LINES = 5000
        private const val MIN_LINES = 3
        private const val STATISTIC = "statistic"
        private const val TRADER = "trader"
        fun newInstance(traderStatistic: TraderStatistic, trader: Trader) =
            TraderProfitFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(STATISTIC, traderStatistic)
                    putParcelable(TRADER, trader)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: TraderProfitPresenter

    @ProvidePresenter
    fun providePresenter() =
        TraderProfitPresenter(
            arguments?.get(STATISTIC) as TraderStatistic,
            arguments?.get(TRADER) as Trader
        ).apply {
            App.instance.appComponent.inject(this)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_profit, container, false)

    override fun init() {
        initBarChart()
        initListeners()
    }

    fun initBarChart() {
        with(bar_chart_trader_profit) {
            data = presenter.setupBarChart("2021")          //   <-btn text
            legend.isEnabled = false
            data.setDrawValues(false)
            animateY(3000)
            setDescription("")
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawZeroLine(true)
        }
    }

    fun initListeners() {
        btn_attached_post_show.setOnClickListener {
            presenter.setPinnedTextMode()
        }

        iv_trader_profit_deal_profit_info_icon.setOnClickListener {
            presenter.showDialog()
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

    override fun setPinnedPostText(text: String?) {
        if (text.isNullOrEmpty()) {
            layout_trader_profit_attached_post.visibility = View.GONE
        } else {
            layout_trader_profit_attached_post.visibility = View.VISIBLE
            tv_attached_post_header.visibility = View.GONE
            iv_attached_post_kebab.visibility = View.GONE
            layout_attached_post_body.visibility = View.VISIBLE
            tv_attached_post_text.text = text
        }
    }

    override fun setPinnedTextVisible(isOpen: Boolean) {
        if (isOpen) {
            btn_attached_post_show.text = resources.getString(R.string.hide)
            tv_attached_post_text.maxLines = MAX_LINES
        } else {
            btn_attached_post_show.text = resources.getString(R.string.show)
            tv_attached_post_text.maxLines = MIN_LINES
        }
    }

    override fun setAverageDealsTime(dealsTime: String) {
        tv_trader_profit_deal_time_value.text = dealsTime
    }

    override fun setAverageDealsPositiveCountAndProfit(averageProfit: String) {
        tv_trader_profit_deal_profit_positive_value.text = averageProfit
    }

    override fun setAverageDealsNegativeCountAndProfit(averageProfit: String) {
        tv_trader_profit_deal_profit_negative_value.text = averageProfit
    }

    override fun setJanProfit(profit: String) {
        tv_trader_profit_jan_value.text = profit
    }

    override fun setFebProfit(profit: String) {
        tv_trader_profit_feb_value.text = profit
    }

    override fun setMarProfit(profit: String) {
        tv_trader_profit_mar_value.text = profit
    }

    override fun setAprProfit(profit: String) {
        tv_trader_profit_apr_value.text = profit
    }

    override fun setMayProfit(profit: String) {
        tv_trader_profit_may_value.text = profit
    }

    override fun setJunProfit(profit: String) {
        tv_trader_profit_jun_value.text = profit
    }

    override fun setJulProfit(profit: String) {
        tv_trader_profit__jul_value.text = profit
    }

    override fun setAugProfit(profit: String) {
        tv_trader_profit_aug_value.text = profit
    }

    override fun setSepProfit(profit: String) {
        tv_trader_profit_sep_value.text = profit
    }

    override fun setOctProfit(profit: String) {
        tv_trader_profit_oct_value.text = profit
    }

    override fun setNovProfit(profit: String) {
        tv_trader_profit_nov_value.text = profit
    }

    override fun setDecProfit(profit: String) {
        tv_trader_profit_dec_value.text = profit
    }

    override fun showInfoDialog() {
        AlertDialog.Builder(context)
            .setMessage(getString(R.string.dialog_info_text))
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
            }.show()
    }
}