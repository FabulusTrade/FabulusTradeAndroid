package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberMainPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberMainView
import ru.wintrade.ui.App

class SubscriberMainFragment : MvpAppCompatFragment(), SubscriberMainView {
    companion object {
        fun newInstance() = SubscriberMainFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberMainPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberMainPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_main, container, false)

    override fun init() {

    }
}