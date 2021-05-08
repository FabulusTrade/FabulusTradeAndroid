package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.QuestionPresenter
import ru.wintrade.mvp.view.QuestionView
import ru.wintrade.ui.App

class QuestionFragment : MvpAppCompatFragment(), QuestionView {
    companion object {
        fun newInstance() = QuestionFragment()
    }

    @InjectPresenter
    lateinit var presenter: QuestionPresenter

    @ProvidePresenter
    fun providePresenter() = QuestionPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_question, container, false)

    override fun init() {
        drawerSetUnlockMode()
    }

    private fun drawerSetUnlockMode() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        requireActivity().toolbar_blue.visibility = View.VISIBLE
    }
}