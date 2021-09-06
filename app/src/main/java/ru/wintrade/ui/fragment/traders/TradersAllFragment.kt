package ru.wintrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var presenter: TradersAllPresenter

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
        adapter = TradersAllRVAdapter(presenter.listPresenter)
        rv_all_traders.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rv_all_traders.layoutManager = layoutManager
        rv_all_traders.addOnScrollListener(
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

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }
}