package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.EntrancePresenter
import ru.wintrade.mvp.view.EntranceView
import ru.wintrade.ui.App

class EntranceFragment: MvpAppCompatFragment(), EntranceView {
    companion object {
        fun newInstance() = EntranceFragment()
    }

    @InjectPresenter
    lateinit var presenter: EntrancePresenter

    @ProvidePresenter
    fun providePresenter() = EntrancePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_entrance, container, false)

    override fun init() {

    }
}