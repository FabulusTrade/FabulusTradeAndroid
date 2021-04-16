package ru.wintrade.ui.fragment.trader

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_trader_main.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.trader.TraderMainPresenter
import ru.wintrade.mvp.view.trader.TraderStatView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.ui.adapter.TraderMainVPAdapter
import ru.wintrade.util.loadImage

class TraderMainFragment(val trader: Trader? = null) : MvpAppCompatFragment(), TraderStatView,
    BackButtonListener {
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
        initViewPager()
        initTraderFields()
        btn_trader_stat_subscribe.setOnClickListener {
            presenter.subscribeToTraderBtnClicked()
        }
        cb_trader_stat_observe.setOnClickListener {
            presenter.observeBtnClicked()
        }
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

    private fun initTraderFields() {
        presenter.trader.avatar?.let { loadImage(it, iv_trader_stat_ava) }
        tv_trader_stat_name.text = presenter.trader.username
        if (presenter.trader.yearProfit?.substring(0, 1) == "-") {
            tv_trader_stat_profit.text = presenter.trader.yearProfit
            tv_trader_stat_profit.setTextColor(Color.RED)
        } else {
            tv_trader_stat_profit.text = presenter.trader.yearProfit
            tv_trader_stat_profit.setTextColor(Color.GREEN)
        }
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

    override fun backClicked(): Boolean {
        presenter.backClicked()
        return true
    }
}