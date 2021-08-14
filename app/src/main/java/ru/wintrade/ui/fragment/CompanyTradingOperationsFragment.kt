package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_company_trading_operations.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.CompanyTradingOperationsPresenter
import ru.wintrade.mvp.view.CompanyTradingOperationsView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.CompanyTradingOperationsRVAdapter

class CompanyTradingOperationsFragment(private val traderId: String, private val companyId: Int) :
    MvpAppCompatFragment(), CompanyTradingOperationsView {
    companion object {
        fun newInstance(traderId: String, companyId: Int) =
            CompanyTradingOperationsFragment(traderId, companyId)
    }

    @InjectPresenter
    lateinit var presenter: CompanyTradingOperationsPresenter

    @ProvidePresenter
    fun providePresenter() = CompanyTradingOperationsPresenter(traderId, companyId).apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: CompanyTradingOperationsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_company_trading_operations, container, false)

    override fun init() {
        adapter = CompanyTradingOperationsRVAdapter(presenter.listPresenter)
        rv_comp_trading_ops.adapter = adapter
        rv_comp_trading_ops.layoutManager = LinearLayoutManager(context)
    }

    override fun updateRecyclerView() {
        adapter?.notifyDataSetChanged()
    }

    override fun setCompanyName(name: String?) {
        tv_comp_trading_ops_name.text = name
    }
}