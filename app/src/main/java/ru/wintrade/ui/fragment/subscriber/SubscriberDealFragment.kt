package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberDealPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberDealView
import ru.wintrade.ui.App

class SubscriberDealFragment : MvpAppCompatFragment(), SubscriberDealView {
    companion object {
        fun newInstance() = SubscriberDealFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberDealPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberDealPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_deal, container, false)

    override fun init() {

    }
}