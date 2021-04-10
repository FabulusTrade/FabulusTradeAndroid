package ru.wintrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_traders_main.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traders.TradersMainPresenter
import ru.wintrade.mvp.view.traders.TradersMainView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.ui.adapter.TradersMainVPAdapter

class TradersMainFragment : MvpAppCompatFragment(), TradersMainView, BackButtonListener {
    companion object {
        fun newInstance() = TradersMainFragment()
    }

    @InjectPresenter
    lateinit var mainPresenter: TradersMainPresenter

    @ProvidePresenter
    fun providePresenter() = TradersMainPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_traders_main, container, false)

    override fun init() {
        vp_main_traders.adapter = TradersMainVPAdapter(this)
        TabLayoutMediator(tab_layout_main_traders, vp_main_traders) { tab, pos ->
            when (pos) {
                0 -> tab.text = resources.getString(R.string.show_all)
                1 -> tab.text = resources.getString(R.string.filter)
            }
        }.attach()
    }

    override fun backClicked(): Boolean {
        mainPresenter.backClicked()
        return true
    }
}