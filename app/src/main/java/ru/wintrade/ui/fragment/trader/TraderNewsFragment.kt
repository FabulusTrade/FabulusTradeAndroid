package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderNewsPresenter
import ru.wintrade.mvp.view.trader.TraderNewsView
import ru.wintrade.ui.App

class TraderNewsFragment : MvpAppCompatFragment(), TraderNewsView {
    companion object {
        fun newInstance() = TraderNewsFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderNewsPresenter

    @ProvidePresenter
    fun providePresenter() = TraderNewsPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_news, container, false)

    override fun init() {

    }
}