package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_trader_trade.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderTradePresenter
import ru.wintrade.mvp.view.trader.TraderDealView
import ru.wintrade.ui.App

class TraderTradeFragment : MvpAppCompatFragment(), TraderDealView {
    companion object {
        fun newInstance() = TraderTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderTradePresenter

    @ProvidePresenter
    fun providePresenter() = TraderTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_trade, container, false)

    override fun init() {
        initListeners()
    }

    fun initListeners() {
        btn_trader_trade_deals.setOnClickListener {
            presenter.myDealsBtnClicked()
        }
        btn_trader_trade_orders.setOnClickListener {
            presenter.myOrdersBtnClicked()
        }
        btn_trader_trade_journal.setOnClickListener {
            presenter.myJournalBtnClicked()
        }
        btn_trader_trade_entry.setOnClickListener {
            presenter.openSignInScreen()
        }
        btn_trader_trade_registration.setOnClickListener {
            presenter.openSignUpScreen()
        }
    }

    override fun setBtnState(state: TraderTradePresenter.State) {
        when (state) {
            TraderTradePresenter.State.MY_DEALS -> {
                myDealsStateInit()
            }
            TraderTradePresenter.State.MY_ORDERS -> {
                myOrdersStateInit()
            }
            TraderTradePresenter.State.MY_JOURNAL -> {
                myJournalStateInit()
            }
        }
    }

    private fun myJournalStateInit() {
        isActive(btn_trader_trade_journal)
        isNotActive(btn_trader_trade_deals)
        isNotActive(btn_trader_trade_orders)
        rv_trader_trade_deals.visibility = View.GONE
        rv_trader_trade_orders.visibility = View.GONE
        rv_trader_trade_journal.visibility = View.VISIBLE
        tv_trader_deal_monthly_counter.visibility = View.GONE
    }

    private fun myOrdersStateInit() {
        isActive(btn_trader_trade_orders)
        isNotActive(btn_trader_trade_deals)
        isNotActive(btn_trader_trade_journal)
        rv_trader_trade_deals.visibility = View.GONE
        rv_trader_trade_orders.visibility = View.VISIBLE
        rv_trader_trade_journal.visibility = View.GONE
        tv_trader_deal_monthly_counter.visibility = View.GONE
    }

    private fun myDealsStateInit() {
        isActive(btn_trader_trade_deals)
        isNotActive(btn_trader_trade_orders)
        isNotActive(btn_trader_trade_journal)
        rv_trader_trade_deals.visibility = View.VISIBLE
        rv_trader_trade_orders.visibility = View.GONE
        rv_trader_trade_journal.visibility = View.GONE
        tv_trader_deal_monthly_counter.visibility = View.VISIBLE
    }

    private fun isActive(activeBtn: MaterialButton) {
        activeBtn.apply {
            backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.colorLightGreen) }
            setTextColor(context?.let {
                ContextCompat.getColorStateList(
                    it,
                    R.color.colorPrimary
                )
            })
        }
    }

    private fun isNotActive(inactiveBtn: MaterialButton) {
        inactiveBtn.apply {
            backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.colorWhite) }
            setTextColor(context?.let {
                ContextCompat.getColorStateList(
                    it,
                    R.color.colorGray
                )
            })
        }
    }

    override fun updateRecyclerView() {

    }

    override fun isAuthorized(isAuth: Boolean) {
        if (isAuth) {
            layout_trader_trade_is_auth.visibility = View.VISIBLE
            layout_trader_trade_not_auth.visibility = View.GONE
        } else {
            layout_trader_trade_is_auth.visibility = View.GONE
            layout_trader_trade_not_auth.visibility = View.VISIBLE
        }
    }
}