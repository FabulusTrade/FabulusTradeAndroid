package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderMainBinding
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.presenter.trader.TraderMainPresenter
import ru.wintrade.mvp.view.trader.TraderMainView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TraderMainVPAdapter
import ru.wintrade.util.loadImage
import ru.wintrade.util.setDrawerLockMode
import ru.wintrade.util.setTextAndColor
import ru.wintrade.util.showLongToast

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
                with((view as CheckBox).isChecked) {
                    presenter.observeBtnClicked(this)
                    val context = requireContext()
                    if (this) {
                        context.showLongToast(
                            context.getResources().getString(R.string.added_to_observation),
                            Toast.LENGTH_SHORT
                        )
                    } else {
                        context.showLongToast(
                            context.getResources().getString(R.string.removed_from_observation),
                            Toast.LENGTH_SHORT
                        )
                    }
                }

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