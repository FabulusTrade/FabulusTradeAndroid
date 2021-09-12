package ru.wintrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTradersMainBinding
import ru.wintrade.mvp.presenter.traders.TradersMainPresenter
import ru.wintrade.mvp.view.traders.TradersMainView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.ui.adapter.TradersMainVPAdapter
import ru.wintrade.util.setDrawerLockMode
import ru.wintrade.util.setToolbarVisible

class TradersMainFragment : MvpAppCompatFragment(), TradersMainView, BackButtonListener {
    private var _binding: FragmentTradersMainBinding? = null
    private val binding: FragmentTradersMainBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = TradersMainFragment()
    }

    @InjectPresenter
    lateinit var mainPresenter: TradersMainPresenter

    @ProvidePresenter
    fun providePresenter() = TradersMainPresenter().apply {
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

    override fun init() {
        initView()
        binding.vpMainTraders.adapter = TradersMainVPAdapter(this)
        TabLayoutMediator(binding.tabLayoutMainTraders, binding.vpMainTraders) { tab, pos ->
            when (pos) {
                0 -> tab.text = resources.getString(R.string.show_all)
                1 -> tab.text = resources.getString(R.string.filter)
            }
        }.attach()
    }

    fun initListeners() {
        tv_traders_main_registration_start.setOnClickListener {
            mainPresenter.openRegistrationScreen()
        }
    }

    override fun setRegistrationBtnVisible(isVisible: Boolean) {
        if (isVisible) {
            tv_traders_main_registration_start.visibility = View.VISIBLE
        } else {
            tv_traders_main_registration_start.visibility = View.GONE
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