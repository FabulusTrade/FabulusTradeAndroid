package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_trader_me_observation.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeObservationPresenter
import ru.wintrade.mvp.view.traderme.TraderMeObservationView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.ObservationRVAdapter


class TraderMeObservationFragment : MvpAppCompatFragment(), TraderMeObservationView {
    companion object {
        fun newInstance() = TraderMeObservationFragment()
        const val DEALS = 1
        const val ORDERS = 2
        const val JOURNALS = 3
    }

    @InjectPresenter
    lateinit var presenter: TraderMeObservationPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeObservationPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: ObservationRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_observation, container, false)

    override fun init() {
        initAdapter()
        initListeners()
    }

    fun initAdapter() {
        adapter = ObservationRVAdapter(presenter.listPresenter)
        rv_trader_me_sub.adapter = adapter
        rv_trader_me_sub.layoutManager = LinearLayoutManager(context)
    }

    fun initListeners() {
        btn_trader_me_sub_deal_trades.setOnClickListener {
            presenter.openTraderMeSubScreen(DEALS)
        }
        btn_trader_me_sub_deal_orders.setOnClickListener {
            presenter.openTraderMeSubScreen(ORDERS)
        }
        btn_trader_me_sub_deal_logs.setOnClickListener {
            presenter.openTraderMeSubScreen(JOURNALS)
        }
    }

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }
}