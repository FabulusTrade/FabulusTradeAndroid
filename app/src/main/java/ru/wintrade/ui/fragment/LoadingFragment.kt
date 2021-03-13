package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_loading.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.LoadingPresenter
import ru.wintrade.mvp.view.LoadingView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.LoadingVPAdapter

class LoadingFragment : MvpAppCompatFragment(), LoadingView {

    companion object {
        fun newInstance() = LoadingFragment()
    }

    private var adapter: LoadingVPAdapter? = null

    @InjectPresenter
    lateinit var presenter: LoadingPresenter

    @ProvidePresenter
    fun providePresenter() = LoadingPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_loading, container, false)

    override fun init() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        requireActivity().toolbar_blue.visibility = View.GONE

        adapter = LoadingVPAdapter(presenter.loadingListPresenter)
        vp_loading.adapter = adapter

        vp_loading.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                presenter.pageChanged(position)
                super.onPageSelected(position)
            }
        })
    }

    override fun updateAdapter() {
        adapter?.notifyDataSetChanged()
    }

    override fun setupTabs(size: Int) {
        for (i in 1..size)
            tab_loading.addTab(tab_loading.newTab())
    }

    override fun tabChanged(pos: Int) {
        tab_loading.getTabAt(pos)?.select()
    }
}