package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderAnalyticsBinding
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter
import ru.wintrade.mvp.view.trader.TraderProfitView
import ru.wintrade.ui.App


class TraderProfitFragment : MvpAppCompatFragment(), TraderProfitView {
    private var _binding: FragmentTraderAnalyticsBinding? = null
    private val binding: FragmentTraderAnalyticsBinding
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
        _binding = FragmentTraderAnalyticsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initBarChart()
        initListeners()
    }

    fun initBarChart() {
        with(binding.barChart) {
            data =
                presenter.setupBarChart(getString(R.string.year_2021_traderProfit))          //   <-btn text
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
            attachedPost.btnAttachedPostShow.setOnClickListener {
                presenter.setPinnedTextMode()
            }
        }
    }

    override fun setDateJoined(date: String) {
        binding.tvRegistrationDateValue.text = date
    }

    override fun setFollowersCount(followersCount: Int) {
        binding.followerCounter.text = followersCount.toString()
    }

    override fun setTradesCount(tradesCount: Int) {
        binding.dealForMonthCount.text = tradesCount.toString()
    }

    override fun setPinnedPostText(text: String?) {
        binding.run {
            if (text.isNullOrEmpty()) {
                attachedPost.root.visibility = View.GONE
            } else {
                with(attachedPost) {
                    root.visibility = View.VISIBLE
                    tvAttachedPostHeader.visibility = View.GONE
                    ivAttachedPostKebab.visibility = View.GONE
                    layoutAttachedPost.visibility = View.VISIBLE
                    layoutAttachedPostBody.visibility = View.VISIBLE
                    tvAttachedPostText.text = text
                }
            }
        }
    }

    override fun setPinnedTextVisible(isOpen: Boolean) {
        binding.attachedPost.run {
            if (isOpen) {
                btnAttachedPostShow.text = resources.getString(R.string.hide_traderProfit)
                tvAttachedPostText.maxLines = MAX_LINES
            } else {
                btnAttachedPostShow.text = resources.getString(R.string.show_traderProfit)
                tvAttachedPostText.maxLines = MIN_LINES
            }
        }
    }

    override fun setAverageDealsTime(dealsTime: String) {
    }

    override fun setAverageDealsPositiveCountAndProfit(averageProfit: String) {
    }

    override fun setAverageDealsNegativeCountAndProfit(averageProfit: String) {
    }

    override fun setJanProfit(profit: String) {
        binding.tvJanValue.text = profit
    }

    override fun setFebProfit(profit: String) {
        binding.tvFebValue.text = profit
    }

    override fun setMarProfit(profit: String) {
        binding.tvMarValue.text = profit
    }

    override fun setAprProfit(profit: String) {
        binding.tvAprValue.text = profit
    }

    override fun setMayProfit(profit: String) {
        binding.tvMayValue.text = profit
    }

    override fun setJunProfit(profit: String) {
        binding.tvJunValue.text = profit
    }

    override fun setJulProfit(profit: String) {
        binding.tvJulValue.text = profit
    }

    override fun setAugProfit(profit: String) {
        binding.tvAugValue.text = profit
    }

    override fun setSepProfit(profit: String) {
        binding.tvSepValue.text = profit
    }

    override fun setOctProfit(profit: String) {
        binding.tvOctValue.text = profit
    }

    override fun setNovProfit(profit: String) {
        binding.tvNovValue.text = profit
    }

    override fun setDecProfit(profit: String) {
        binding.tvDecValue.text = profit
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}