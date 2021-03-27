package ru.wintrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traders.TradersFilterPresenter
import ru.wintrade.mvp.view.traders.TradersFilterView
import ru.wintrade.ui.App

class TradersFilterFragment : MvpAppCompatFragment(), TradersFilterView {
    companion object {
        fun newInstance() = TradersFilterFragment()
    }

    @InjectPresenter
    lateinit var presenter: TradersFilterPresenter

    @ProvidePresenter
    fun providePresenter() = TradersFilterPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_traders_filter, container, false)

    override fun init() {

    }
}