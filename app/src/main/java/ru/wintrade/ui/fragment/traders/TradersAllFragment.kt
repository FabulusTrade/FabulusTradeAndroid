package ru.wintrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_traders_all.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traders.TradersAllPresenter
import ru.wintrade.mvp.view.traders.TradersAllView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TradersAllRVAdapter

class TradersAllFragment : MvpAppCompatFragment(), TradersAllView {
    companion object {
        fun newInstance() = TradersAllFragment()
    }

    private var adapter: TradersAllRVAdapter? = null

    @InjectPresenter
    lateinit var allPresenter: TradersAllPresenter

    @ProvidePresenter
    fun providePresenter() = TradersAllPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_traders_all, container, false)

    override fun init() {
        adapter = TradersAllRVAdapter(allPresenter.listPresenter)
        rv_all_traders.adapter = adapter
        rv_all_traders.layoutManager = LinearLayoutManager(context)
    }

    override fun updateRecyclerView() {
        adapter?.notifyDataSetChanged()
    }
}