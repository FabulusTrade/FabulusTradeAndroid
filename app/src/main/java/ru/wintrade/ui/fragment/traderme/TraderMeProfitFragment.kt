package ru.wintrade.ui.fragment.traderme

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderMeProfitBinding
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.presenter.traderme.TraderMeProfitPresenter
import ru.wintrade.mvp.view.traderme.TraderMeProfitView
import ru.wintrade.ui.App


class TraderMeProfitFragment : MvpAppCompatFragment(),
    TraderMeProfitView {
    private var _binding: FragmentTraderMeProfitBinding? = null
    private val binding: FragmentTraderMeProfitBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val ANIMATE_DURATION = 3000
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
    ): View? {
        _binding = FragmentTraderMeProfitBinding.inflate(inflater, container, false)
        return _binding?.root
    }

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
        with(binding.barChartTraderMeProfit) {
            data = presenter.setupBarChart(getString(R.string.year_2021))          //   <-btn text
            legend.isEnabled = false
            data.setDrawValues(false)
            animateY(ANIMATE_DURATION)
            setDescription("")
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawZeroLine(true)
        }
    }

    private fun initListeners() {
        binding.run {
            layoutTraderMeProfitAttachedPost.tvAttachedPostHeader.setOnClickListener {
                presenter.openCreatePostScreen(true, null)
            }
            layoutTraderMeProfitAttachedPost.btnAttachedPostShow.setOnClickListener {
                presenter.setPinnedTextMode()
            }
            ivTraderMeProfitDealProfitInfoIcon.setOnClickListener {
                presenter.showDialog()
            }
        }
    }

    private fun initPopupMenu() {
        val popupMenu = androidx.appcompat.widget.PopupMenu(
            requireContext(),
            binding.layoutTraderMeProfitAttachedPost.ivAttachedPostKebab
        )
        popupMenu.inflate(R.menu.menu_pinned_text)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.pinned_text_edit -> {
                    presenter.openCreatePostScreen(
                        null,
                        binding.layoutTraderMeProfitAttachedPost.tvAttachedPostText.text.toString()
                    )
                    true
                }
                R.id.pinned_text_delete -> {
                    presenter.deletePinnedText()
                    binding.layoutTraderMeProfitAttachedPost.layoutAttachedPostBody.visibility =
                        View.GONE
                    binding.layoutTraderMeProfitAttachedPost.tvAttachedPostHeader.visibility =
                        View.VISIBLE
                    true
                }
                else -> false
            }
        }
        binding.layoutTraderMeProfitAttachedPost.ivAttachedPostKebab.setOnClickListener {
            popupMenu.show()
        }
    }

    override fun setDateJoined(date: String) {
        binding.tvTraderMeProfitRegistrationDateValue.text = date
    }

    override fun setFollowersCount(followersCount: Int) {
        binding.tvTraderMeProfitFollowerCounter.text = followersCount.toString()
    }

    override fun setTradesCount(tradesCount: Int) {
        binding.tvTraderMeProfitDealForMonthCount.text = tradesCount.toString()
    }

    override fun setPinnedPostText(text: String?) {
        binding.layoutTraderMeProfitAttachedPost.run {
            if (!text.isNullOrEmpty()) {
                layoutAttachedPostBody.visibility = View.VISIBLE
                tvAttachedPostText.text = text
                tvAttachedPostHeader.visibility = View.GONE
            }
        }
    }

    override fun setPinnedTextVisible(isOpen: Boolean) {
        binding.layoutTraderMeProfitAttachedPost.run {
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
        binding.tvTraderMeProfitDealTimeValue.text = dealsTime
    }

    override fun setAverageDealsPositiveCountAndProfit(averageProfit: String) {
        binding.tvTraderMeProfitDealProfitPositiveValue.text = averageProfit
    }

    override fun setAverageDealsNegativeCountAndProfit(averageProfit: String) {
        binding.tvTraderMeProfitDealProfitNegativeValue.text = averageProfit
    }

    override fun setJanProfit(profit: String) {
        binding.tvTraderMeProfitJanValue.text = profit
    }

    override fun setFebProfit(profit: String) {
        binding.tvTraderMeProfitFebValue.text = profit
    }

    override fun setMarProfit(profit: String) {
        binding.tvTraderMeProfitMarValue.text = profit
    }

    override fun setAprProfit(profit: String) {
        binding.tvTraderMeProfitAprValue.text = profit
    }

    override fun setMayProfit(profit: String) {
        binding.tvTraderMeProfitMayValue.text = profit
    }

    override fun setJunProfit(profit: String) {
        binding.tvTraderMeProfitJunValue.text = profit
    }

    override fun setJulProfit(profit: String) {
        binding.tvTraderMeProfitJulValue.text = profit
    }

    override fun setAugProfit(profit: String) {
        binding.tvTraderMeProfitAugValue.text = profit
    }

    override fun setSepProfit(profit: String) {
        binding.tvTraderMeProfitSepValue.text = profit
    }

    override fun setOctProfit(profit: String) {
        binding.tvTraderMeProfitOctValue.text = profit
    }

    override fun setNovProfit(profit: String) {
        binding.tvTraderMeProfitNovValue.text = profit
    }

    override fun setDecProfit(profit: String) {
        binding.tvTraderMeProfitDecValue.text = profit
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