package ru.fabulus.fabulustrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTradersMainBinding
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersMainPresenter
import ru.fabulus.fabulustrade.mvp.view.traders.TradersMainView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.BackButtonListener
import ru.fabulus.fabulustrade.ui.adapter.TradersMainVPAdapter
import ru.fabulus.fabulustrade.util.setDrawerLockMode
import ru.fabulus.fabulustrade.util.setToolbarVisible

class TradersMainFragment : MvpAppCompatFragment(), TradersMainView, BackButtonListener {
    private var _binding: FragmentTradersMainBinding? = null
    private val binding: FragmentTradersMainBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val CHECKED_FILTER = "main_traders_filter"
        private const val DEFAULT_FILTER = 0
        fun newInstance(checkedFilter: Int?) = TradersMainFragment().apply {
            arguments = Bundle().apply {
                when (checkedFilter) {
                    null -> putInt(CHECKED_FILTER, DEFAULT_FILTER)
                    else -> putInt(CHECKED_FILTER, checkedFilter)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var mainPresenter: TradersMainPresenter

    @ProvidePresenter
    fun providePresenter() = TradersMainPresenter(
        arguments?.get(CHECKED_FILTER) as Int
    ).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradersMainBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init(checkedFilter: Int) {
        initView()
        initListeners()
        binding.vpMainTraders.adapter = TradersMainVPAdapter(this, checkedFilter)
        TabLayoutMediator(binding.tabLayoutMainTraders, binding.vpMainTraders) { tab, pos ->
            when (pos) {
                0 -> tab.text = resources.getString(R.string.show_tradersMain)
                1 -> tab.text = resources.getString(R.string.filter)
            }
        }.attach()
    }

    fun initListeners() {
        binding.run {
            tvTradersMainRegistrationStart.setOnClickListener {
                mainPresenter.openRegistrationScreen(true)
            }
        }
    }

    override fun setRegistrationBtnVisible(isVisible: Boolean) {
        if (isVisible) {
            binding.tvTradersMainRegistrationStart.visibility = View.VISIBLE
        } else {
            binding.tvTradersMainRegistrationStart.visibility = View.GONE
        }
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun backClicked(): Boolean {
        mainPresenter.backClicked()
        return true
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}