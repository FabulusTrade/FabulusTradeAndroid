package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentSettingsBinding
import ru.fabulus.fabulustrade.mvp.presenter.SettingsPresenter
import ru.fabulus.fabulustrade.mvp.view.SettingsView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.setDrawerLockMode
import ru.fabulus.fabulustrade.util.setToolbarVisible

class SettingsFragment : MvpAppCompatFragment(), SettingsView {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    @ProvidePresenter
    fun providePresenter() = SettingsPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
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