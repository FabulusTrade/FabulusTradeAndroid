package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.LoadingPresenter
import ru.wintrade.mvp.view.LoadingView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.PagerAdapter

class LoadingFragment : MvpAppCompatFragment(), LoadingView {

    companion object {
        fun newInstance() = LoadingFragment()
    }

    lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

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
        val fragmentsList =
            arrayListOf<Fragment>(SignUpFragment.newInstance(), OnBoardFragment.newInstance())
        viewPager = view!!.findViewById(R.id.view_pager_loading)
        tabLayout = view!!.findViewById(R.id.tab_main)
        viewPager.adapter = PagerAdapter(this, fragmentsList)
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }

}