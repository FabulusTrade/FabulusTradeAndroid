package ru.wintrade.ui.fragment.trader

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_trader_main.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.trader.TraderMainPresenter
import ru.wintrade.mvp.view.trader.TraderMainView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TraderMainVPAdapter
import ru.wintrade.util.loadImage
import ru.wintrade.util.setDrawerLockMode
import ru.wintrade.util.setToolbarVisible

class TraderMainFragment(val trader: Trader? = null) : MvpAppCompatFragment(), TraderMainView {
    companion object {
        fun newInstance(trader: Trader) = TraderMainFragment(trader)
    }

    @InjectPresenter
    lateinit var presenter: TraderMainPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMainPresenter(trader!!).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_main, container, false)

    override fun init() {
        initView()
        initViewPager()
        btn_trader_stat_subscribe.setOnClickListener {
            presenter.subscribeToTraderBtnClicked()
        }
        cb_trader_stat_observe.setOnClickListener {
            presenter.observeBtnClicked()
        }
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun setSubscribeBtnActive(isActive: Boolean) {
        if (isActive) {
            with(btn_trader_stat_subscribe) {
                visibility = View.VISIBLE
                isEnabled = true
                text = resources.getText(R.string.join)
                backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.colorAccent) }
                (this as MaterialButton).strokeColor =
                    context?.let { ContextCompat.getColorStateList(it, R.color.colorAccent) }
            }
        } else {
            with(btn_trader_stat_subscribe) {
                visibility = View.VISIBLE
                isEnabled = false
                text = resources.getText(R.string.isSubscribe)
                backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.colorWhite) }
                (this as MaterialButton).strokeColor =
                    context?.let { ContextCompat.getColorStateList(it, R.color.colorWhite) }
            }
        }
    }

    override fun setObserveVisibility(isVisible: Boolean) {
        if (isVisible)
            cb_trader_stat_observe.visibility = View.VISIBLE
        else
            cb_trader_stat_observe.visibility = View.INVISIBLE
    }

    override fun setObserveActive(isActive: Boolean) {
        cb_trader_stat_observe.isChecked = isActive
    }

    override fun setUsername(username: String) {
        tv_trader_stat_name.text = username
    }

    override fun setProfit(profit: String, isPositive: Boolean) {
        if (isPositive) {
            tv_trader_stat_profit.text = profit
            tv_trader_stat_profit.setTextColor(Color.GREEN)
        } else {
            tv_trader_stat_profit.text = profit
            tv_trader_stat_profit.setTextColor(Color.RED)
        }
    }

    override fun setAvatar(avatar: String) {
        loadImage(avatar, iv_trader_stat_ava)
    }

    private fun initViewPager() {
        vp_trader_stat.adapter = TraderMainVPAdapter(this, trader!!)
        TabLayoutMediator(
            tab_layout_trader_stat,
            vp_trader_stat
        ) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_trader_profit)
                1 -> tab.setIcon(R.drawable.ic_trader_news)
                2 -> tab.setIcon(R.drawable.ic_trader_instrument)
                3 -> tab.setIcon(R.drawable.ic_trader_deal)
            }
        }.attach()
    }
}