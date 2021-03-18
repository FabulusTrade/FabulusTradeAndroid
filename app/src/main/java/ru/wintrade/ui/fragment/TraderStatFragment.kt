package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.TraderStatPresenter
import ru.wintrade.mvp.view.TraderStatView
import ru.wintrade.ui.App

class TraderStatFragment : MvpAppCompatFragment(), TraderStatView {
    companion object {
        fun newInstance() = TraderStatFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderStatPresenter

    @ProvidePresenter
    fun providePresenter() = TraderStatPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_stat, container, false)

    override fun init() {

    }
}