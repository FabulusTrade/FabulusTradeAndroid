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
import ru.wintrade.ui.adapter.PostRVAdapter

class TraderPostFragment : MvpAppCompatFragment(), TraderPostView {
    companion object {
        const val TRADER = "trader"
        fun newInstance(trader: Trader) =
            TraderPostFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TRADER, trader)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: TraderPostPresenter

    @ProvidePresenter
    fun providePresenter() = TraderPostPresenter(
        arguments?.get(TRADER) as Trader
    ).apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: PostRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_post, container, false)

    override fun init() {
        initRV()
        initListeners()
    }

    fun initRV() {
        adapter = PostRVAdapter(presenter.listPresenter)
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

    fun initListeners() {
        btn_trader_post_entry.setOnClickListener {
            presenter.openSignInScreen()
        }
        btn_trader_post_registration.setOnClickListener {
            presenter.openSignUpScreen()
        }
    }

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }

    override fun isAuthorized(isAuth: Boolean) {
        if (!isAuth) {
            layout_trader_post_is_auth.visibility = View.GONE
            layout_trader_post_not_auth.visibility = View.VISIBLE
        } else {
            layout_trader_post_is_auth.visibility = View.VISIBLE
            layout_trader_post_not_auth.visibility = View.GONE
        }
    }
}