package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_trader_me_subs_trade.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeSubTradePresenter
import ru.wintrade.mvp.view.traderme.TraderMeSubTradeView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.SubscriberTradesRVAdapter

class TraderMeSubTradeFragment(val position: Int) : MvpAppCompatFragment(), TraderMeSubTradeView {
    companion object {
        fun newInstance(position: Int) = TraderMeSubTradeFragment(position)
    }

    @InjectPresenter
    lateinit var presenter: TraderMeSubTradePresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeSubTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    var adapter: SubscriberTradesRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_subs_trade, container, false)

    override fun init() {
        when (position) {
            1 -> presenter.dealsBtnClicked()
            2 -> presenter.ordersBtnClicked()
            3 -> presenter.journalBtnClicked()
        }
        initRecyclerView()
        initListeners()
    }

    fun initRecyclerView() {
        adapter = SubscriberTradesRVAdapter(presenter.listPresenter)
        rv_trader_me_sub_trade_deal.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rv_trader_me_sub_trade_deal.layoutManager = layoutManager
        rv_trader_me_sub_trade_deal.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            presenter.onScrollLimit()
                        }
                    }
                }
            }
        )
    }

    override fun setBtnState(state: TraderMeSubTradePresenter.State) {
        when (state) {
            TraderMeSubTradePresenter.State.DEALS -> dealsStateInit()
            TraderMeSubTradePresenter.State.ORDERS -> ordersStateInit()
            TraderMeSubTradePresenter.State.JOURNAL -> journalStateInit()
        }
    }

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        layout_trader_me_sub_trade_refresh.isRefreshing = isRefreshing
    }

    fun initListeners() {
        layout_trader_me_sub_trade.setOnClickListener {
            presenter.refreshed()
        }
        btn_trader_me_sub_trade_deal.setOnClickListener {
            presenter.dealsBtnClicked()
        }
        btn_trader_me_sub_trade_orders.setOnClickListener {
            presenter.ordersBtnClicked()
        }
        btn_trader_me_sub_trade_logs.setOnClickListener {
            presenter.journalBtnClicked()
        }
    }

    private fun journalStateInit() {
        isActive(btn_trader_me_sub_trade_logs)
        isNotActive(btn_trader_me_sub_trade_deal)
        isNotActive(btn_trader_me_sub_trade_orders)
        layout_trader_me_sub_trade.visibility = View.GONE
        tv_trader_me_sub_trade_coming_soon.visibility = View.VISIBLE
    }

    private fun ordersStateInit() {
        isActive(btn_trader_me_sub_trade_orders)
        isNotActive(btn_trader_me_sub_trade_deal)
        isNotActive(btn_trader_me_sub_trade_logs)
        layout_trader_me_sub_trade.visibility = View.GONE
        tv_trader_me_sub_trade_coming_soon.visibility = View.VISIBLE
    }

    private fun dealsStateInit() {
        isActive(btn_trader_me_sub_trade_deal)
        isNotActive(btn_trader_me_sub_trade_orders)
        isNotActive(btn_trader_me_sub_trade_logs)
        layout_trader_me_sub_trade.visibility = View.VISIBLE
        tv_trader_me_sub_trade_coming_soon.visibility = View.GONE
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