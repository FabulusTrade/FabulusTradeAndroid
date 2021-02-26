package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.SplashPresenter
import ru.wintrade.mvp.view.SplashView
import ru.wintrade.ui.App

class SplashFragment : MvpAppCompatFragment(), SplashView {

    companion object {
        fun newInstance() = SplashFragment()
    }

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    @ProvidePresenter
    fun providePresenter() = SplashPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun init() {
    }
}