package ru.fabulus.fabulustrade.ui.fragment.entrance

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentResetPasswordBinding
import ru.fabulus.fabulustrade.mvp.presenter.registration.subscriber.ResetPasswordPresenter
import ru.fabulus.fabulustrade.mvp.view.registration.subscriber.ResetPasswordView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.EmailValidation

class ResetPasswordFragment : MvpAppCompatFragment(), ResetPasswordView {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding: FragmentResetPasswordBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = ResetPasswordFragment()
    }

    @InjectPresenter
    lateinit var presenter: ResetPasswordPresenter

    @ProvidePresenter
    fun providePresenter() = ResetPasswordPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        binding.run {
            btnResetPass.setOnClickListener {
                presenter.resetPassBtnClicked(etResetPassEmail.text.toString())
            }
        }
    }

    override fun setEmailError(validation: EmailValidation) {
        binding.etResetPassLayout.error = when (validation) {
            EmailValidation.INCORRECT -> getString(R.string.email_incorrect)
            else -> null
        }
    }

    override fun showSuccessDialog() {
        AlertDialog.Builder(context)
            .setMessage(getString(R.string.reset_pass_success))
            .setCancelable(false)
            .setPositiveButton(R.string.ok_resetFragment) { _, _ ->
                presenter.openSignInScreen()
            }.show()
    }

    override fun showAlertDialog() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.error_resetFragment))
            .setMessage(getString(R.string.reset_pass_email_not_registred))
            .setCancelable(false)
            .setPositiveButton(R.string.ok_resetFragment) { _, _ ->
            }.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}