package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
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
        when (state) {
            TraderPostPresenter.State.PUBLICATIONS -> {
                publicationsStateInit()
            }
            TraderPostPresenter.State.SUBSCRIPTION -> {
                subscriptionStateInit()
            }
        }
    }

    private fun subscriptionStateInit() {
        isActive(btn_trader_news_subscription)
        isNotActive(btn_trader_news_publication)
        rv_trader_news_subscription.visibility = View.VISIBLE
        layout_trader_news_title.visibility = View.GONE
        tv_trader_news_coming_soon.visibility = View.GONE
    }

    private fun publicationsStateInit() {
        isActive(btn_trader_news_publication)
        isNotActive(btn_trader_news_subscription)
        rv_trader_news_subscription.visibility = View.GONE
        layout_trader_news_title.visibility = View.VISIBLE
        tv_trader_news_coming_soon.visibility = View.VISIBLE
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

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }
}