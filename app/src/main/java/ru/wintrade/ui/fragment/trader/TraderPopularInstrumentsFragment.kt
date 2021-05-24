package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trader_popular_instruments.*
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
        initListeners()
    }

    fun initListeners() {
        btn_trader_pop_instr_entry.setOnClickListener {
            presenter.openSignInScreen()
        }
        btn_trader_pop_instr_registration.setOnClickListener {
            presenter.openSignUpScreen()
        }
    }

    override fun isAuthorized(isAuth: Boolean) {
        if (isAuth) {
            layout_trader_pop_instr_is_auth.visibility = View.VISIBLE
            layout_trader_pop_instr_not_auth.visibility = View.GONE
        } else {
            layout_trader_pop_instr_is_auth.visibility = View.GONE
            layout_trader_pop_instr_not_auth.visibility = View.VISIBLE
        }
    }
}