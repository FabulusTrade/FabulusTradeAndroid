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
import ru.wintrade.mvp.presenter.trader.CompanyTradingOperationsPresenter
import ru.wintrade.mvp.view.trader.CompanyTradingOperationsView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.CompanyTradingOperationsRVAdapter

class CompanyTradingOperationsFragment : MvpAppCompatFragment(), CompanyTradingOperationsView {
    companion object {
        fun newInstance() = CompanyTradingOperationsFragment()
    }

    @InjectPresenter
    lateinit var presenter: CompanyTradingOperationsPresenter

    @ProvidePresenter
    fun providePresenter() = CompanyTradingOperationsPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: CompanyTradingOperationsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_deals_detail, container, false)

    override fun init() {
        adapter = CompanyTradingOperationsRVAdapter(presenter.listPresenter)
        rv_trader_deals_detail.adapter = adapter
        rv_trader_deals_detail.layoutManager = LinearLayoutManager(context)
    }
}