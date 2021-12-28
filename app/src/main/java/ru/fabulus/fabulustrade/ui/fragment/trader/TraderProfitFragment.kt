package ru.fabulus.fabulustrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTraderAnalyticsBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderProfitPresenter
import ru.fabulus.fabulustrade.mvp.view.trader.TraderProfitView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.setTextAndColor


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

    private fun initBarChart() {
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
            btnForYear.setOnClickListener {
                presenter.getStatisticForYear()
            }
            btnForFiftyDeals.setOnClickListener {
                presenter.getStatisticLastFifty()
            }
        }
    }

    override fun setDateJoined(date: String) {
        binding.tvRegistrationDateValue.text = date
    }

    override fun setFollowersCount(followersCount: String) {
        binding.tvFollowerCounter.text = followersCount
    }

    override fun setTradesCount(tradesCount: String) {
        binding.tvDealForMonthCount.text = tradesCount
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

    override fun setBtnsState(state: TraderProfitPresenter.State) {
        when (state) {
            TraderProfitPresenter.State.FOR_YEAR -> {
                setForYearState()
            }
            TraderProfitPresenter.State.LAST_FIFTY -> {
                setLastFiftyState()
            }
        }
    }

    private fun setForYearState() {
        binding.run {
            isActive(btnForYear)
            isNotActive(btnForFiftyDeals)
        }
    }

    private fun setLastFiftyState() {
        binding.run {
            isActive(btnForFiftyDeals)
            isNotActive(btnForYear)
        }
    }

    private fun isNotActive(inactiveBtn: MaterialButton) {
        inactiveBtn.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)
            setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorGray))
        }
    }

    private fun isActive(activeBtn: MaterialButton) {
        activeBtn.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorLightGreen)
            setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary))
        }
    }

    override fun setAverageDealsTime(dealsTime: String) {
        binding.tvDealAverageTimeValue.text = dealsTime
    }

    override fun setJanProfit(profit: String, textColor: Int) {
        binding.tvJanValue.setTextAndColor(profit, textColor)
    }

    override fun setFebProfit(profit: String, textColor: Int) {
        binding.tvFebValue.setTextAndColor(profit, textColor)
    }

    override fun setMarProfit(profit: String, textColor: Int) {
        binding.tvMarValue.setTextAndColor(profit, textColor)
    }

    override fun setAprProfit(profit: String, textColor: Int) {
        binding.tvAprValue.setTextAndColor(profit, textColor)
    }

    override fun setMayProfit(profit: String, textColor: Int) {
        binding.tvMayValue.setTextAndColor(profit, textColor)
    }

    override fun setJunProfit(profit: String, textColor: Int) {
        binding.tvJunValue.setTextAndColor(profit, textColor)
    }

    override fun setJulProfit(profit: String, textColor: Int) {
        binding.tvJulValue.setTextAndColor(profit, textColor)
    }

    override fun setAugProfit(profit: String, textColor: Int) {
        binding.tvAugValue.setTextAndColor(profit, textColor)
    }

    override fun setSepProfit(profit: String, textColor: Int) {
        binding.tvSepValue.setTextAndColor(profit, textColor)
    }

    override fun setOctProfit(profit: String, textColor: Int) {
        binding.tvOctValue.setTextAndColor(profit, textColor)
    }

    override fun setNovProfit(profit: String, textColor: Int) {
        binding.tvNovValue.setTextAndColor(profit, textColor)
    }

    override fun setDecProfit(profit: String, textColor: Int) {
        binding.tvDecValue.setTextAndColor(profit, textColor)
    }

    override fun setPositiveProfitPercentForTransactions(percent: String) {
        binding.tvDealProfitPositiveValue.text = percent
    }

    override fun setNegativeProfitPercentForTransactions(percent: String) {
        binding.tvDealProfitNegativeValue.text = percent
    }

    override fun setAverageProfitForDeal(percent: String) {
        binding.tvAverageProfitValue.text = percent
    }

    override fun setAverageLoseForDeal(percent: String) {
        binding.tvAverageLossValue.text = percent
    }

    override fun setDepoValue(percent: String, textColor: Int) {
        binding.tvDepoValue.setTextAndColor(percent, textColor)
    }

    override fun setAllDealLong(percent: String) {
        binding.tvAllDealLong.text = percent
    }

    override fun setAllDealShort(percent: String) {
        binding.tvAllDealShort.text = percent
    }

    override fun setAvaregeTimeDealLong(daysCount: String) {
        binding.tvAverageTimeDealLong.text = daysCount
    }

    override fun setAvaregeTimeDealShort(daysCount: String) {
        binding.tvAverageTimeDealShort.text = daysCount
    }

    override fun setPercentOfProfitDealsLong(percent: String) {
        binding.tvProfDealLong.text = percent
    }

    override fun setPercentOfProfitDealsShort(percent: String) {
        binding.tvProfDealShort.text = percent
    }

    override fun setAvaregePercentForProfitDealLong(percent: String) {
        binding.tvAveragePercentProfitDealLong.text = percent
    }

    override fun setAvaregePercentForProfitDealShort(percent: String) {
        binding.tvAveragePercentProfitDealShort.text = percent
    }

    override fun setPercentOfLosingDealsLong(percent: String) {
        binding.tvLessDealLong.text = percent
    }

    override fun setPercentOfLosingDealsShort(percent: String) {
        binding.tvLessDealShort.text = percent
    }

    override fun setAvaregePercentForLosingDealLong(percent: String) {
        binding.tvAverageLessDealLong.text = percent
    }

    override fun setAvaregePercentForLosingDealShort(percent: String) {
        binding.tvAverageLessDealShort.text = percent
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}