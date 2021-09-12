package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentAboutWtBinding
import ru.wintrade.mvp.presenter.AboutWinTradePresenter
import ru.wintrade.mvp.view.AboutWinTradeView
import ru.wintrade.ui.App
import ru.wintrade.util.setDrawerLockMode
import ru.wintrade.util.setToolbarVisible


class AboutWinTradeFragment : MvpAppCompatFragment(), AboutWinTradeView {
    private var _binding: FragmentAboutWtBinding? = null
    private val binding: FragmentAboutWtBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = AboutWinTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: AboutWinTradePresenter

    @ProvidePresenter
    fun providePresenter() = AboutWinTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutWtBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initView()
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}