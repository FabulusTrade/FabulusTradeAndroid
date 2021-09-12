package ru.wintrade.ui.fragment.trader

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderProfitBinding
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter
import ru.wintrade.mvp.view.trader.TraderProfitView
import ru.wintrade.ui.App


class TraderProfitFragment : MvpAppCompatFragment(), TraderProfitView {
    private var _binding: FragmentTraderProfitBinding? = null
    private val binding: FragmentTraderProfitBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val ANIMATE_DURATION = 3000
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
    ): View? {
        _binding = FragmentTraderProfitBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initBarChart()
        initListeners()
    }

    fun initBarChart() {
        with(binding.barChartTraderProfit) {
            data = presenter.setupBarChart(getString(R.string.year_2021))          //   <-btn text
            legend.isEnabled = false
            data.setDrawValues(false)
            animateY(ANIMATE_DURATION)
            setDescription("")
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawZeroLine(true)
        }
    }

    fun initListeners() {
        binding.run {
            layoutTraderProfitAttachedPost.btnAttachedPostShow.setOnClickListener {
                presenter.setPinnedTextMode()
            }
            ivTraderProfitDealProfitInfoIcon.setOnClickListener {
                presenter.showDialog()
            }
        }
    }

    override fun setDateJoined(date: String) {
        binding.tvTraderProfitRegistrationDateValue.text = date
    }

    override fun setFollowersCount(followersCount: Int) {
        binding.tvTraderProfitFollowerCounter.text = followersCount.toString()
    }

    override fun setTradesCount(tradesCount: Int) {
        binding.tvTraderProfitDealForWeekCount.text = tradesCount.toString()
    }

    override fun setPinnedPostText(text: String?) {
        binding.run {
            if (text.isNullOrEmpty()) {
                layoutTraderProfitAttachedPost.root.visibility = View.GONE
            } else {
                with(layoutTraderProfitAttachedPost) {
                    root.visibility = View.VISIBLE
                    tvAttachedPostHeader.visibility = View.GONE
                    ivAttachedPostKebab.visibility = View.GONE
                    layoutAttachedPost.visibility = View.VISIBLE
                    tvAttachedPostText.text = text
                }
            }
        }
    }

    override fun setPinnedTextVisible(isOpen: Boolean) {
        binding.layoutTraderProfitAttachedPost.run {
            if (isOpen) {
                btnAttachedPostShow.text = resources.getString(R.string.hide)
                tvAttachedPostText.maxLines = MAX_LINES
            } else {
                btnAttachedPostShow.text = resources.getString(R.string.show)
                tvAttachedPostText.maxLines = MIN_LINES
            }
        }
    }

    override fun setAverageDealsTime(dealsTime: String) {
        binding.tvTraderProfitDealTimeValue.text = dealsTime
    }

    override fun setAverageDealsPositiveCountAndProfit(averageProfit: String) {
        binding.tvTraderProfitDealProfitPositiveValue.text = averageProfit
    }

    override fun setAverageDealsNegativeCountAndProfit(averageProfit: String) {
        binding.tvTraderProfitDealProfitNegativeValue.text = averageProfit
    }

    override fun setJanProfit(profit: String) {
        binding.tvTraderProfitJanValue.text = profit
    }

    override fun setFebProfit(profit: String) {
        binding.tvTraderProfitFebValue.text = profit
    }

    override fun setMarProfit(profit: String) {
        binding.tvTraderProfitMarValue.text = profit
    }

    override fun setAprProfit(profit: String) {
        binding.tvTraderProfitAprValue.text = profit
    }

    override fun setMayProfit(profit: String) {
        binding.tvTraderProfitMayValue.text = profit
    }

    override fun setJunProfit(profit: String) {
        binding.tvTraderProfitJunValue.text = profit
    }

    override fun setJulProfit(profit: String) {
        binding.tvTraderProfitJulValue.text = profit
    }

    override fun setAugProfit(profit: String) {
        binding.tvTraderProfitAugValue.text = profit
    }

    override fun setSepProfit(profit: String) {
        binding.tvTraderProfitSepValue.text = profit
    }

    override fun setOctProfit(profit: String) {
        binding.tvTraderProfitOctValue.text = profit
    }

    override fun setNovProfit(profit: String) {
        binding.tvTraderProfitNovValue.text = profit
    }

    override fun setDecProfit(profit: String) {
        binding.tvTraderProfitDecValue.text = profit
    }

    override fun showInfoDialog() {
        AlertDialog.Builder(context)
            .setMessage(getString(R.string.dialog_info_text))
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
            }.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}