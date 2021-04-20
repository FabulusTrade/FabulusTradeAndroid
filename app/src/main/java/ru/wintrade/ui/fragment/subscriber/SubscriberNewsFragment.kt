package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberPostPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberNewsView
import ru.wintrade.ui.App

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_news, container, false)

    override fun init() {

    }
}