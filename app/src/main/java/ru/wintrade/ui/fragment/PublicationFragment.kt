package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.PublicationPresenter
import ru.wintrade.mvp.view.PublicationView
import ru.wintrade.ui.App

class PublicationFragment : MvpAppCompatFragment(), PublicationView {
    companion object {
        fun newInstance() = QuestionFragment()
    }

    @InjectPresenter
    lateinit var presenter: PublicationPresenter

    @ProvidePresenter
    fun providePresenter() = PublicationPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_publication, container, false)

    override fun init() {

    }
}