package ru.fabulus.fabulustrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTraderMainBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderMainPresenter
import ru.fabulus.fabulustrade.mvp.view.trader.TraderMainView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.TraderMainVPAdapter
import ru.fabulus.fabulustrade.util.*

class TraderMainFragment : MvpAppCompatFragment(), TraderMainView {
    private var _binding: FragmentTraderMainBinding? = null
    private val binding: FragmentTraderMainBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        const val TRADER = "trader"
        fun newInstance(trader: Trader) = TraderMainFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TRADER, trader)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: TraderMainPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMainPresenter(
        arguments?.get(TRADER) as Trader
    ).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderMainBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initView()
        initListeners()
    }

    private fun initListeners() {
        binding.run {
            btnTraderStatSubscribe.setOnClickListener {
                presenter.subscribeToTraderBtnClicked()
                binding.cbTraderStatObserve.visibility = View.INVISIBLE
            }
            cbTraderStatObserve.setOnClickListener { view ->
                presenter.observeBtnClicked((view as CheckBox).isChecked)
            }
        }
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun initVP(traderStatistic: TraderStatistic, trader: Trader) {
        binding.vpTraderStat.adapter = TraderMainVPAdapter(this, traderStatistic, trader)
        TabLayoutMediator(
            binding.tabLayoutTraderStat,
            binding.vpTraderStat
        ) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_trader_profit)
                1 -> tab.setIcon(R.drawable.ic_trader_news)
                2 -> tab.setIcon(R.drawable.ic_trader_instrument)
                3 -> tab.setIcon(R.drawable.ic_trader_deal)
            }
        }.attach()
    }

    override fun setSubscribeBtnActive(isActive: Boolean) {
        with(binding.btnTraderStatSubscribe) {
            visibility = View.INVISIBLE
            isClickable = isActive
            if (isActive) {
                text = resources.getText(R.string.join)
                backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorAccent)
                (this as MaterialButton).strokeColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorAccent)
                binding.cbTraderStatObserve.visibility = View.VISIBLE
            } else {
                text = resources.getText(R.string.isSubscribe)
                backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)
                (this as MaterialButton).strokeColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)
                binding.cbTraderStatObserve.visibility = View.INVISIBLE
            }

            visibility = View.VISIBLE
        }
    }

    override fun setObserveVisibility(isVisible: Boolean) {
        binding.cbTraderStatObserve.visibility.apply {
            if (isVisible)
                View.VISIBLE
            else
                View.INVISIBLE
        }
    }

    override fun setObserveActive(isActive: Boolean) {
        binding.cbTraderStatObserve.isActivated = isActive
    }

    override fun setObserveChecked(isChecked: Boolean) {
        binding.cbTraderStatObserve.isChecked = isChecked
    }

    override fun showToast(text: String, duration: Int) {
        requireContext().showToast(text, duration)
    }

    override fun setUsername(username: String) {
        binding.tvTraderStatName.text = username
    }

    override fun setProfit(profit: String, textColor: Int) {
        binding.tvTraderStatProfit.setTextAndColor(profit, textColor)
    }

    override fun setAvatar(avatar: String) {
        loadImage(avatar, binding.ivTraderStatAva)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}