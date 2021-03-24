package ru.wintrade.ui.activity

import android.content.Intent
import android.os.Bundle
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.mvp.presenter.SplashPresenter
import ru.wintrade.mvp.view.SplashView
import ru.wintrade.ui.App

class SplashActivity: MvpAppCompatActivity(), SplashView {

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    @ProvidePresenter
    fun providePresenter() = SplashPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.appComponent.inject(this)
    }

    override fun init() {}

    override fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}