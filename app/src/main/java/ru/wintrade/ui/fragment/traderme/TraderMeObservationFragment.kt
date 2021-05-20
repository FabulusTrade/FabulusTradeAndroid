package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeObservationPresenter
import ru.wintrade.mvp.view.traderme.TraderMeObservationView
import ru.wintrade.ui.App


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_observation, container, false)

    override fun init() {

    }
}