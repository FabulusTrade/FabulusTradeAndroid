package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_subscriber_observation.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeObservationPresenter
import ru.wintrade.mvp.view.traderme.TraderMeObservationView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.ObservationRVAdapter


class TraderMeObservationFragment : MvpAppCompatFragment(), TraderMeObservationView {
    companion object {
        fun newInstance() = TraderMeObservationFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMeObservationPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeObservationPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: ObservationRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_observation, container, false)

    override fun init() {
        adapter = ObservationRVAdapter(presenter.listPresenter)
        rv_sub_obs.adapter = adapter
        rv_sub_obs.layoutManager = LinearLayoutManager(context)
    }

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }
}