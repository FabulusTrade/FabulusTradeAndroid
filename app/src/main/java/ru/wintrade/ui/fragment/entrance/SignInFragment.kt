package ru.wintrade.ui.fragment.entrance

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.drawerlayout.widget.DrawerLayout
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentSignInBinding
import ru.wintrade.mvp.presenter.entrance.SignInPresenter
import ru.wintrade.mvp.view.entrance.SignInView
import ru.wintrade.ui.App
import ru.wintrade.util.*

class SignInFragment : MvpAppCompatFragment(), SignInView {
    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val IS_AS_TRADER_REGISTRATION = "registration"
        fun newInstance(isSubscriber: Boolean) = SignInFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_AS_TRADER_REGISTRATION, isSubscriber)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: SignInPresenter

    @ProvidePresenter
    fun providePresenter() =
        SignInPresenter(arguments?.get(IS_AS_TRADER_REGISTRATION) as Boolean).apply {
            App.instance.appComponent.inject(this)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init(isAsTraderRegistration: Boolean) {
        initView()
        initListeners(isAsTraderRegistration)
        if (isAsTraderRegistration) {
            binding.entranceRegistrationButton.text =
                resources.getString(R.string.registration_button_title_second)
        } else {
            binding.entranceRegistrationButton.text =
                resources.getString(R.string.registration_button_title_first)
        }
    }

    fun initListeners(asTraderRegistration: Boolean) {
        binding.run {
            btnSignInResetPass.setOnClickListener { presenter.openResetPassScreen() }
            entranceRegistrationButton.setOnClickListener { presenter.openRegistrationScreen(asTraderRegistration) }
            entranceEnterButton.setOnClickListener { enterBtnClicked() }
        }
    }

    private fun enterBtnClicked() {
        presenter.loginBtnClicked(
            binding.etSignInNickname.text.toString(),
            binding.etSignInPassword.text.toString()
        )
        (requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            0
        )
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setToolbarVisible(false)
    }

    override fun setAccess(isAuthorized: Boolean) {
        val pref = requireActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        with(pref.edit()) {
            putBoolean(ru.wintrade.util.IS_AUTHORIZED, isAuthorized)
            apply()
        }
    }

    override fun showToast(toast: String) {
        requireContext().showLongToast(toast)
    }

    override fun setAppToolbarMenuVisible(visible: Boolean) {
        setToolbarMenuVisible(visible)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}