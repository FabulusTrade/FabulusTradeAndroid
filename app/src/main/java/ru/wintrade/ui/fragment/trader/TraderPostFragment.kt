package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_trader_post.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.trader.TraderPostPresenter
import ru.wintrade.mvp.view.trader.TraderPostView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TraderNewsRVAdapter

class TraderPostFragment(val trader: Trader): MvpAppCompatFragment(), TraderPostView {
    companion object {
        fun newInstance(trader: Trader) = TraderPostFragment(trader)
    }

    @InjectPresenter
    lateinit var presenter: TraderPostPresenter

    @ProvidePresenter
    fun providePresenter() = TraderPostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: TraderNewsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_post, container, false)

    override fun init() {
        adapter = TraderNewsRVAdapter(presenter.listPresenter)
        rv_trader_post.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rv_trader_post.layoutManager = layoutManager
        rv_trader_post.addOnScrollListener(
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