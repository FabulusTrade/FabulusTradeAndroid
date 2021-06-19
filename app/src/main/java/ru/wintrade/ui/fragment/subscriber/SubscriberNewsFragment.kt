package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_subscriber_news.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberPostPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberNewsView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.PostRVAdapter

class SubscriberNewsFragment : MvpAppCompatFragment(), SubscriberNewsView {
    companion object {
        fun newInstance() = SubscriberNewsFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberPostPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberPostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: PostRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_news, container, false)

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun init() {
        adapter = PostRVAdapter(presenter.listPresenter)
        rv_subscriber_news.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rv_subscriber_news.layoutManager = layoutManager
        rv_subscriber_news.addOnScrollListener(
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