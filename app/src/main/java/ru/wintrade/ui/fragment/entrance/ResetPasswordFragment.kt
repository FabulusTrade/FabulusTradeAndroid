package ru.wintrade.ui.fragment.entrance

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_reset_password.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.registration.subscriber.ResetPasswordPresenter
import ru.wintrade.mvp.view.registration.subscriber.ResetPasswordView
import ru.wintrade.ui.App
import ru.wintrade.util.EmailValidation

class ResetPasswordFragment : MvpAppCompatFragment(), ResetPasswordView {
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
    ): View? = inflater.inflate(R.layout.fragment_reset_password, container, false)

    override fun init() {
        btn_reset_pass.setOnClickListener {
            presenter.resetPassBtnClicked(et_reset_pass_email.text.toString())
        }
    }

    override fun setEmailError(validation: EmailValidation) {
        et_reset_pass_layout.error = when (validation) {
            EmailValidation.INCORRECT -> getString(R.string.email_incorrect)
            else -> null
        }
    }

    override fun showSuccessDialog() {
        AlertDialog.Builder(context)
            .setMessage(getString(R.string.reset_pass_success))
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                presenter.openSignInScreen()
            }.show()
    }

    override fun showAlertDialog() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.error))
            .setMessage(getString(R.string.reset_pass_email_not_registred))
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
            }.show()
    }
}