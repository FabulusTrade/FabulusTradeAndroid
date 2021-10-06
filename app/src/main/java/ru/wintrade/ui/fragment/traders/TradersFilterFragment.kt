package ru.wintrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTradersFilterBinding
import ru.wintrade.mvp.presenter.traders.TradersFilterPresenter
import ru.wintrade.mvp.view.traders.TradersFilterView
import ru.wintrade.ui.App

class TradersFilterFragment : MvpAppCompatFragment(), TradersFilterView {
    private var _binding: FragmentTradersFilterBinding? = null
    private val binding: FragmentTradersFilterBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val CHECKED_FILTER = "filter_screen_checked_filter"
        fun newInstance(checkedFilter: Int?) = TradersFilterFragment().apply {
            arguments = Bundle().apply {
                putInt(CHECKED_FILTER, checkedFilter!!)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: TradersFilterPresenter

    @ProvidePresenter
    fun providePresenter() = TradersFilterPresenter(
        arguments?.get(CHECKED_FILTER) as Int
    ).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradersFilterBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        binding.run {
            btnFilterApply.setOnClickListener {
                presenter.applyFilter()
            }
            cbFilterByProfit.setOnClickListener {
                presenter.byProfitBoxClicked()
            }
            cbFilterByFollowers.setOnClickListener {
                presenter.byFollowersBoxClicked()
            }
        }
    }

    override fun setFilterCheckBoxState(checkedFilter: TradersFilterPresenter.CheckedFilter) {
        when (checkedFilter) {
            TradersFilterPresenter.CheckedFilter.BY_PROFIT -> {
                with(binding) {
                    cbFilterByProfit.isChecked = true
                    cbFilterByFollowers.isChecked = false
                }
            }
            TradersFilterPresenter.CheckedFilter.BY_FOLLOWERS -> {
                with(binding) {
                    cbFilterByProfit.isChecked = false
                    cbFilterByFollowers.isChecked = true
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}