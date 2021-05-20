package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_trader_me_posts.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.wintrade.mvp.view.trader.TraderMePostView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TraderNewsRVAdapter

class TraderMePostFragment : MvpAppCompatFragment(), TraderMePostView {
    companion object {
        fun newInstance() = TraderMePostFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMePostPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMePostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: TraderNewsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_posts, container, false)

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun init() {
        adapter = TraderNewsRVAdapter(presenter.listPresenter)
        rv_trader_me_post.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rv_trader_me_post.layoutManager = layoutManager
        rv_trader_me_post.addOnScrollListener(
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

        layout_trader_news_title.setOnClickListener {
            presenter.onCreatePostBtnClicked()
        }

        btn_trader_news_publication.setOnClickListener {
            presenter.publicationsBtnClicked()
        }
        btn_trader_news_subscription.setOnClickListener {
            presenter.subscriptionBtnClicked()
        }
    }

    override fun setBtnsState(state: TraderMePostPresenter.State) {
        when (state) {
            TraderMePostPresenter.State.PUBLICATIONS -> {
                publicationsStateInit()
            }
            TraderMePostPresenter.State.SUBSCRIPTION -> {
                subscriptionStateInit()
            }
        }
    }

    private fun subscriptionStateInit() {
        isActive(btn_trader_news_subscription)
        isNotActive(btn_trader_news_publication)
        layout_trader_news_title.visibility = View.GONE
    }

    private fun publicationsStateInit() {
        isActive(btn_trader_news_publication)
        isNotActive(btn_trader_news_subscription)
        layout_trader_news_title.visibility = View.VISIBLE
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