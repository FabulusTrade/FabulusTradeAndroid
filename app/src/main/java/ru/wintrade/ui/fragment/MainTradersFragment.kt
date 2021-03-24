package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_traders.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.MainTradersPresenter
import ru.wintrade.mvp.view.MainTradersView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.ui.adapter.MainTradersVPAdapter

class MainTradersFragment : MvpAppCompatFragment(), MainTradersView, BackButtonListener {
    companion object {
        fun newInstance() = MainTradersFragment()
    }

    @InjectPresenter
    lateinit var presenter: MainTradersPresenter

    @ProvidePresenter
    fun providePresenter() = MainTradersPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main_traders, container, false)

    override fun init() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        requireActivity().toolbar_blue.visibility = View.VISIBLE
        vp_main_traders.adapter = MainTradersVPAdapter(this, presenter.listPresenter)
        TabLayoutMediator(tab_layout_main_traders, vp_main_traders) { tab, pos ->
            when (pos) {
                0 -> tab.text = resources.getString(R.string.show_all)
                1 -> tab.text = resources.getString(R.string.filter)
            }
        }.attach()
    }

    override fun backClicked(): Boolean {
        presenter.backClicked()
        return true
    }
}