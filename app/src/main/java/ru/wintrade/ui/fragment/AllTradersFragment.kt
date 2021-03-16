package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_all_traders.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.Traders
import ru.wintrade.mvp.presenter.AllTradersPresenter
import ru.wintrade.mvp.view.AllTradersView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.AllTradersRVAdapter

class AllTradersFragment : MvpAppCompatFragment(), AllTradersView {
    companion object {
        fun newInstance() = AllTradersFragment()
    }

    private var adapter: AllTradersRVAdapter? = null

    @InjectPresenter
    lateinit var presenter: AllTradersPresenter

    @ProvidePresenter
    fun providePresenter() = AllTradersPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_all_traders, container, false)

    override fun init() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        requireActivity().toolbar_blue.visibility = View.VISIBLE
    }

    override fun renderData(trader: List<Traders>) {
        adapter = AllTradersRVAdapter(presenter.listPresenter)
        traders_recycler_view.adapter = adapter
        traders_recycler_view.layoutManager = LinearLayoutManager(context)
    }
}