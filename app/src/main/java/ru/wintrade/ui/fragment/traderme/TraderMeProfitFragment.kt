package ru.wintrade.ui.fragment.traderme

import android.app.AlertDialog
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
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.presenter.traderme.TraderMeProfitPresenter
import ru.wintrade.mvp.view.traderme.TraderMeProfitView
import ru.wintrade.ui.App

class TraderMeProfitFragment : MvpAppCompatFragment(),
    TraderMeProfitView {
    companion object {
        private const val MAX_LINES = 5000
        private const val MIN_LINES = 3
        private const val STATISTIC = "statistic"
        fun newInstance(traderStatistic: TraderStatistic) =
            TraderMeProfitFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(STATISTIC, traderStatistic)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: TraderMeProfitPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeProfitPresenter(
        arguments?.get(STATISTIC) as TraderStatistic
    ).apply {
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
        initBarChart()
        initListeners()
        initPopupMenu()
    }

    private fun initBarChart() {
        with(bar_chart_trader_me_profit) {
            data = presenter.setupBarChart("2021")          //   <-btn text
            legend.isEnabled = false
            data.setDrawValues(false)
            animateY(3000)
            setDescription("")
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawZeroLine(true)
        }
    }

    private fun initListeners() {
        tv_attached_post_header.setOnClickListener {
            presenter.openCreatePostScreen(true, null)
        }

        btn_attached_post_show.setOnClickListener {
            presenter.setPinnedTextMode()
        }

        iv_trader_me_profit_deal_profit_info_icon.setOnClickListener {
            presenter.showDialog()
        }
    }

    private fun initPopupMenu() {
        val popupMenu =
            context?.let { androidx.appcompat.widget.PopupMenu(it, iv_attached_post_kebab) }
        popupMenu?.inflate(R.menu.menu_pinned_text)
        popupMenu?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.pinned_text_edit -> {
                    presenter.openCreatePostScreen(null, tv_attached_post_text.text.toString())
                    true
                }
                R.id.pinned_text_delete -> {
                    presenter.deletePinnedText()
                    layout_attached_post_body.visibility = View.GONE
                    tv_attached_post_header.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }
        iv_attached_post_kebab.setOnClickListener {
            popupMenu?.show()
        }
    }

    override fun setDateJoined(date: String) {
        tv_trader_me_profit_registration_date_value.text = date
    }

    override fun setFollowersCount(followersCount: Int) {
        tv_trader_me_profit_follower_counter.text = followersCount.toString()
    }

    override fun setTradesCount(tradesCount: Int) {
        tv_trader_me_profit_deal_for_month_count.text = tradesCount.toString()
    }

    override fun setPinnedPostText(text: String?) {
        if (!text.isNullOrEmpty()) {
            layout_attached_post_body.visibility = View.VISIBLE
            tv_attached_post_text.text = text
            tv_attached_post_header.visibility = View.GONE
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
        tv_trader_me_profit_deal_time_value.text = dealsTime
    }

    override fun setAverageDealsPositiveCountAndProfit(averageProfit: String) {
        tv_trader_me_profit_deal_profit_positive_value.text = averageProfit
    }

    override fun setAverageDealsNegativeCountAndProfit(averageProfit: String) {
        tv_trader_me_profit_deal_profit_negative_value.text = averageProfit
    }

    override fun setJanProfit(profit: String) {
        tv_trader_me_profit_jan_value.text = profit
    }

    override fun setFebProfit(profit: String) {
        tv_trader_me_profit_feb_value.text = profit
    }

    override fun setMarProfit(profit: String) {
        tv_trader_me_profit_mar_value.text = profit
    }

    override fun setAprProfit(profit: String) {
        tv_trader_me_profit_apr_value.text = profit
    }

    override fun setMayProfit(profit: String) {
        tv_trader_me_profit_may_value.text = profit
    }

    override fun setJunProfit(profit: String) {
        tv_trader_me_profit_jun_value.text = profit
    }

    override fun setJulProfit(profit: String) {
        tv_trader_me_profit_jul_value.text = profit
    }

    override fun setAugProfit(profit: String) {
        tv_trader_me_profit_aug_value.text = profit
    }

    override fun setSepProfit(profit: String) {
        tv_trader_me_profit_sep_value.text = profit
    }

    override fun setOctProfit(profit: String) {
        tv_trader_me_profit_oct_value.text = profit
    }

    override fun setNovProfit(profit: String) {
        tv_trader_me_profit_nov_value.text = profit
    }

    override fun setDecProfit(profit: String) {
        tv_trader_me_profit_dec_value.text = profit
    }

    override fun showInfoDialog() {
        AlertDialog.Builder(context)
            .setMessage(getString(R.string.dialog_info_text))
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
            }.show()
    }
}