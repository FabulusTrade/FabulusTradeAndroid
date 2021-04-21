package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberPostPresenter
import ru.wintrade.mvp.presenter.trader.TraderObservationPresenter
import ru.wintrade.mvp.view.trader.TraderObservationView
import ru.wintrade.ui.App


class TraderObservationFragment : MvpAppCompatFragment(), TraderObservationView {
    companion object {
        fun newInstance() = TraderObservationFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderObservationPresenter

    @ProvidePresenter
    fun providePresenter() = TraderObservationPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_observation, container, false)

    override fun init() {

    }
}