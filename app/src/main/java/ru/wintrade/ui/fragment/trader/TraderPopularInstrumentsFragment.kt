package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderPopularInstrumentsPresenter
import ru.wintrade.mvp.view.trader.TraderPopularInstrumentsView
import ru.wintrade.ui.App

class TraderPopularInstrumentsFragment : MvpAppCompatFragment(), TraderPopularInstrumentsView {
    companion object {
        fun newInstance() = TraderPopularInstrumentsFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderPopularInstrumentsPresenter

    @ProvidePresenter
    fun providePresenter() = TraderPopularInstrumentsPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_popular_instruments, container, false)

    override fun init() {

    }
}