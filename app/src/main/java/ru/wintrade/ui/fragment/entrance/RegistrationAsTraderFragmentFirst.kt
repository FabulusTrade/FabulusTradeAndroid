package ru.wintrade.ui.fragment.entrance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentRegistrationAsTraderFirstBinding
import ru.wintrade.mvp.model.entity.SignUpData
import ru.wintrade.mvp.presenter.registration.trader.RegAsTraderFirstPresenter
import ru.wintrade.mvp.view.registration.trader.RegAsTraderFirstView
import ru.wintrade.ui.App
import ru.wintrade.util.REGISTRATION_DATA
import ru.wintrade.util.setToolbarVisible

class RegistrationAsTraderFragmentFirst : MvpAppCompatFragment(), RegAsTraderFirstView {
    companion object {
        fun newInstance(signUpData: SignUpData) =
            RegistrationAsTraderFragmentFirst().apply {
                arguments = Bundle().apply {
                    putParcelable(REGISTRATION_DATA, signUpData)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: RegAsTraderFirstPresenter

    @ProvidePresenter
    fun providePresenter() = RegAsTraderFirstPresenter(
        arguments?.get(REGISTRATION_DATA) as SignUpData
    ).apply {
        App.instance.appComponent.inject(this)
    }

    private var _binding: FragmentRegistrationAsTraderFirstBinding? = null
    private val binding: FragmentRegistrationAsTraderFirstBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationAsTraderFirstBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initListeners()
        setToolbarVisible(false)
    }

    private fun initListeners() {
        binding.btnContinueTraderReg1.setOnClickListener {
            presenter.openRegistrationSecondScreen()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}