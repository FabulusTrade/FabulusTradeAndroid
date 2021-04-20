package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_trader_deals_detail.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderDealsDetailPresenter
import ru.wintrade.mvp.view.trader.TraderDealsDetailView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.DealsDetailRVAdapter

class TraderDealsDetailFragment : MvpAppCompatFragment(), TraderDealsDetailView {
    companion object {
        fun newInstance() = TraderDealsDetailFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderDealsDetailPresenter

    @ProvidePresenter
    fun providePresenter() = TraderDealsDetailPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: DealsDetailRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_deals_detail, container, false)

    override fun init() {
        adapter = DealsDetailRVAdapter(presenter.listPresenter)
        rv_trader_deals_detail.adapter = adapter
        rv_trader_deals_detail.layoutManager = LinearLayoutManager(context)
    }
}