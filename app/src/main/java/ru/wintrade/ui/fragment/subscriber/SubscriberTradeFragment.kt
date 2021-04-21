package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_subscriber_deal.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberTradePresenter
import ru.wintrade.mvp.view.subscriber.SubscriberDealView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.SubscriberTradesRVAdapter

class SubscriberTradeFragment : MvpAppCompatFragment(), SubscriberDealView {
    companion object {
        fun newInstance() = SubscriberTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberTradePresenter

    var adapter: SubscriberTradesRVAdapter? = null

    @ProvidePresenter
    fun providePresenter() = SubscriberTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_deal, container, false)

    override fun init() {
        adapter = SubscriberTradesRVAdapter(presenter.listPresenter)
        rv_sub_deal.adapter = adapter
        rv_sub_deal.layoutManager = LinearLayoutManager(context)
        layout_sub_deal_refresh.setOnRefreshListener {
            presenter.refreshed()
        }
        btn_sub_deal_trades.setOnClickListener {
            presenter.dealsBtnClicked()
        }
        btn_sub_deal_orders.setOnClickListener {
            presenter.ordersBtnClicked()
        }
        btn_sub_deal_logs.setOnClickListener {
            presenter.journalBtnClicked()
        }
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        layout_sub_deal_refresh.isRefreshing = isRefreshing
    }

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }


    override fun setBtnState(state: SubscriberTradePresenter.State) {
        when (state) {
            SubscriberTradePresenter.State.DEALS -> {
                dealsStateInit()
            }
            SubscriberTradePresenter.State.ORDERS -> {
                ordersStateInit()
            }
            SubscriberTradePresenter.State.JOURNAL -> {
                journalStateInit()
            }
        }
    }

    private fun dealsStateInit() {
        isActive(btn_sub_deal_trades)
        isNotActive(btn_sub_deal_orders)
        isNotActive(btn_sub_deal_logs)
        layout_sub_deal_refresh.visibility = View.VISIBLE
        tv_sub_deal_coming_soon.visibility = View.GONE
    }

    private fun ordersStateInit() {
        isNotActive(btn_sub_deal_trades)
        isActive(btn_sub_deal_orders)
        isNotActive(btn_sub_deal_logs)
        layout_sub_deal_refresh.visibility = View.GONE
        tv_sub_deal_coming_soon.visibility = View.VISIBLE
    }

    private fun journalStateInit() {
        isNotActive(btn_sub_deal_trades)
        isNotActive(btn_sub_deal_orders)
        isActive(btn_sub_deal_logs)
        layout_sub_deal_refresh.visibility = View.GONE
        tv_sub_deal_coming_soon.visibility = View.VISIBLE
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