package ru.wintrade.ui.fragment.traderforsubscriber

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_trader_for_subscriber_main.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.traderforsubscriber.TraderForSubscriberMainPresenter
import ru.wintrade.mvp.view.traderforsubscriber.TraderForSubscriberMainView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.ui.adapter.TraderForSubscriberMainVPAdapter
import ru.wintrade.util.IS_AUTHORIZED
import ru.wintrade.util.PREFERENCE_NAME
import ru.wintrade.util.loadImage

class TraderForSubscriberMainFragment(val trader: Trader? = null) : MvpAppCompatFragment(),
    TraderForSubscriberMainView,
    BackButtonListener {
    companion object {
        fun newInstance(trader: Trader) = TraderForSubscriberMainFragment(trader)
    }

    @InjectPresenter
    lateinit var presenter: TraderForSubscriberMainPresenter

    @ProvidePresenter
    fun providePresenter() = TraderForSubscriberMainPresenter(trader!!, isAccessed()).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_for_subscriber_main, container, false)

    override fun init() {
        drawerSetMode()
        initViewPager()
        initTraderFields()
        btn_tr_for_sub_stat_subscribe.setOnClickListener {
            presenter.subscribeToTraderBtnClicked(isAccessed())
        }
        cb_tr_for_sub_stat_observe.setOnClickListener {
            presenter.observeBtnClicked(isAccessed())
        }
    }

    private fun drawerSetMode() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        requireActivity().toolbar_blue.visibility = View.VISIBLE
    }

    override fun setSubscribeBtnActive(isActive: Boolean) {
        if (isActive) {
            with(btn_tr_for_sub_stat_subscribe) {
                visibility = View.VISIBLE
                isEnabled = true
                text = resources.getText(R.string.join)
                backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.colorAccent) }
                (this as MaterialButton).strokeColor =
                    context?.let { ContextCompat.getColorStateList(it, R.color.colorAccent) }
            }
        } else {
            with(btn_tr_for_sub_stat_subscribe) {
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
            cb_tr_for_sub_stat_observe.visibility = View.VISIBLE
        else
            cb_tr_for_sub_stat_observe.visibility = View.INVISIBLE
    }

    override fun setObserveActive(isActive: Boolean) {
        cb_tr_for_sub_stat_observe.isChecked = isActive
    }

    private fun initTraderFields() {
        presenter.trader.avatar?.let { loadImage(it, iv_tr_for_sub_stat_ava) }
        tv_tr_for_sub_stat_name.text = presenter.trader.username
        if (presenter.trader.yearProfit?.substring(0, 1) == "-") {
            tv_tr_for_sub_stat_profit.text = presenter.trader.yearProfit
            tv_tr_for_sub_stat_profit.setTextColor(Color.RED)
        } else {
            tv_tr_for_sub_stat_profit.text = presenter.trader.yearProfit
            tv_tr_for_sub_stat_profit.setTextColor(Color.GREEN)
        }
    }

    private fun initViewPager() {
        vp_tr_for_sub_stat.adapter = TraderForSubscriberMainVPAdapter(this, trader!!)
        TabLayoutMediator(
            tab_layout_tr_for_sub_stat,
            vp_tr_for_sub_stat
        ) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_trader_profit)
                1 -> tab.setIcon(R.drawable.ic_trader_news)
                2 -> tab.setIcon(R.drawable.ic_trader_instrument)
                3 -> tab.setIcon(R.drawable.ic_trader_deal)
            }
        }.attach()
    }

    override fun backClicked(): Boolean {
        presenter.backClicked()
        return true
    }

    fun isAccessed(): Boolean {
        return requireActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getBoolean(
                IS_AUTHORIZED, false
            )
    }
}