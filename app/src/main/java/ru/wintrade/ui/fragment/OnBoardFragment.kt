package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.OnBoardPresenter
import ru.wintrade.mvp.view.OnBoardView
import ru.wintrade.ui.App

class OnBoardFragment: MvpAppCompatFragment(), OnBoardView {

    companion object {
        fun newInstance() = OnBoardFragment()
    }

    @InjectPresenter
    lateinit var presenter: OnBoardPresenter

    @ProvidePresenter
    fun providePresenter() = OnBoardPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_on_board, container, false)

    override fun init() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        requireActivity().toolbar.visibility = View.VISIBLE
    }
}