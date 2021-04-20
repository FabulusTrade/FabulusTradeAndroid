package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_trader_posts.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderPostPresenter
import ru.wintrade.mvp.view.trader.TraderPostView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TraderNewsRVAdapter

class TraderPostFragment : MvpAppCompatFragment(), TraderPostView {
    companion object {
        fun newInstance() = TraderPostFragment()
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
    ): View? = inflater.inflate(R.layout.fragment_trader_posts, container, false)

    override fun init() {
        adapter = TraderNewsRVAdapter(presenter.listPresenter)
        rv_trader_news_subscription.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rv_trader_news_subscription.layoutManager = layoutManager
        rv_trader_news_subscription.addOnScrollListener(
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

        btn_trader_news_publication.setOnClickListener {
            presenter.publicationsBtnClicked()
        }
        btn_trader_news_subscription.setOnClickListener {
            presenter.subscriptionBtnClicked()
        }
    }

    override fun setBtnsState(state: TraderPostPresenter.State) {
        val activeBtn: Button
        val inactiveBtn: Button
        if (state == TraderPostPresenter.State.PUBLICATIONS) {
            activeBtn = btn_trader_news_publication
            inactiveBtn = btn_trader_news_subscription
        } else {
            activeBtn = btn_trader_news_subscription
            inactiveBtn = btn_trader_news_publication
        }
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

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }

    override fun setVisibility(isVisible: Boolean) {
        if (isVisible) {
            rv_trader_news_subscription.visibility = View.GONE
//            rv_trader_news_publication.visibility = View.VISIBLE
        } else {
//            rv_trader_news_publication.visibility = View.GONE
            rv_trader_news_subscription.visibility = View.VISIBLE
        }
    }
}