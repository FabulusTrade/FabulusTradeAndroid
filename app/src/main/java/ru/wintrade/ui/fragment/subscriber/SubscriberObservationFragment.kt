package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_subscriber_observation.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberObservationPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberObservationView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.SubscriberObservationRVAdapter

class SubscriberObservationFragment : MvpAppCompatFragment(), SubscriberObservationView {
    companion object {
        fun newInstance() = SubscriberObservationFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberObservationPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberObservationPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: SubscriberObservationRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_observation, container, false)

    override fun init() {
        adapter = SubscriberObservationRVAdapter(presenter.listPresenter)
        rv_sub_obs.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rv_sub_obs.layoutManager = layoutManager
        rv_sub_obs.addOnScrollListener(
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