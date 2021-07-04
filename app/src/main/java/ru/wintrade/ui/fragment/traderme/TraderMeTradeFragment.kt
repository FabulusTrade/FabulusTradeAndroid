package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_trader_me_trade.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeTradePresenter
import ru.wintrade.mvp.view.traderme.TraderMeTradeView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.SubscriberTradesRVAdapter
import ru.wintrade.ui.adapter.TradesByCompanyRVAdapter

class TraderMeTradeFragment : MvpAppCompatFragment(), TraderMeTradeView {
    companion object {
        fun newInstance() = TraderMeTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMeTradePresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var tradesAdapter: TradesByCompanyRVAdapter? = null
    private var ordersAdapter: TradesByCompanyRVAdapter? = null
    lateinit var adapter: SubscriberTradesRVAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_trade, container, false)

    override fun init() {
        initListeners()
        initRV()
    }

    fun initListeners() {
        btn_trader_me_trade_deals.setOnClickListener {
            presenter.myDealsBtnClicked()
        }
        btn_trader_me_trade_orders.setOnClickListener {
            presenter.myOrdersBtnClicked()
        }
        btn_trader_me_trade_journal.setOnClickListener {
            presenter.myJournalBtnClicked()
        }
    }

    fun initRV() {
//        tradesAdapter = TradesByCompanyRVAdapter(presenter.tradesListPresenter)
//        ordersAdapter = TradesByCompanyRVAdapter(presenter.ordersListPresenter)
//        rv_trader_me_trade_deals.adapter = tradesAdapter
//        rv_trader_me_trade_deals.layoutManager = LinearLayoutManager(context)
//        rv_trader_me_trade_orders.adapter = ordersAdapter
//        rv_trader_me_trade_orders.layoutManager = LinearLayoutManager(context)
        adapter = SubscriberTradesRVAdapter(presenter.listPresenter)
        rv_trader_me_trade_deals.adapter = adapter
        rv_trader_me_trade_deals.layoutManager = LinearLayoutManager(context)
    }

    override fun setBtnState(state: TraderMeTradePresenter.State) {
        when (state) {
            TraderMeTradePresenter.State.MY_DEALS -> {
                myDealsStateInit()
            }
            TraderMeTradePresenter.State.MY_ORDERS -> {
                myOrdersStateInit()
            }
            TraderMeTradePresenter.State.MY_JOURNAL -> {
                myJournalStateInit()
            }
        }
    }

    override fun updateTradesAdapter() {
//        tradesAdapter?.notifyDataSetChanged()
        adapter?.notifyDataSetChanged()
    }

    override fun updateOrdersAdapter() {
        ordersAdapter?.notifyDataSetChanged()
    }

    private fun myJournalStateInit() {
        isActive(btn_trader_me_trade_journal)
        isNotActive(btn_trader_me_trade_deals)
        isNotActive(btn_trader_me_trade_orders)
        rv_trader_me_trade_deals.visibility = View.GONE
        rv_trader_me_trade_orders.visibility = View.GONE
        rv_trader_me_trade_journal.visibility = View.VISIBLE
        tv_trader_me_deal_monthly_counter.visibility = View.GONE
    }

    private fun myOrdersStateInit() {
        isActive(btn_trader_me_trade_orders)
        isNotActive(btn_trader_me_trade_deals)
        isNotActive(btn_trader_me_trade_journal)
        rv_trader_me_trade_deals.visibility = View.GONE
        rv_trader_me_trade_orders.visibility = View.VISIBLE
        rv_trader_me_trade_journal.visibility = View.GONE
        tv_trader_me_deal_monthly_counter.visibility = View.GONE
    }

    private fun myDealsStateInit() {
        isActive(btn_trader_me_trade_deals)
        isNotActive(btn_trader_me_trade_orders)
        isNotActive(btn_trader_me_trade_journal)
        rv_trader_me_trade_deals.visibility = View.VISIBLE
        rv_trader_me_trade_orders.visibility = View.GONE
        rv_trader_me_trade_journal.visibility = View.GONE
        tv_trader_me_deal_monthly_counter.visibility = View.VISIBLE
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
}