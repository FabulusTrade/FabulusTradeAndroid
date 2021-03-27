package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberObservationPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberObservationView
import ru.wintrade.ui.App

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_observation, container, false)

    override fun init() {

    }
}