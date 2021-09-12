package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderPopularInstrumentsBinding
import ru.wintrade.mvp.presenter.trader.TraderPopularInstrumentsPresenter
import ru.wintrade.mvp.view.trader.TraderPopularInstrumentsView
import ru.wintrade.ui.App

class TraderPopularInstrumentsFragment : MvpAppCompatFragment(), TraderPopularInstrumentsView {
    private var _binding: FragmentTraderPopularInstrumentsBinding? = null
    private val binding: FragmentTraderPopularInstrumentsBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = TraderPopularInstrumentsFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderPopularInstrumentsPresenter

    @ProvidePresenter
    fun providePresenter() = TraderPopularInstrumentsPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderPopularInstrumentsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initListeners()
    }

    fun initListeners() {
        binding.run {
            btnTraderPopInstrEntry.setOnClickListener {
                presenter.openSignInScreen()
            }
            btnTraderPopInstrRegistration.setOnClickListener {
                presenter.openSignUpScreen()
            }
        }
    }

    override fun isAuthorized(isAuth: Boolean) {
        binding.run {
            if (isAuth) {
                layoutTraderPopInstrIsAuth.visibility = View.VISIBLE
                layoutTraderPopInstrNotAuth.visibility = View.GONE
            } else {
                layoutTraderPopInstrIsAuth.visibility = View.GONE
                layoutTraderPopInstrNotAuth.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}