package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.layout_title.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.SignInPresenter
import ru.wintrade.mvp.view.SignInView
import ru.wintrade.ui.App

class SignInFragment : MvpAppCompatFragment(), SignInView {
    companion object {
        fun newInstance() = SignInFragment()
    }

    @InjectPresenter
    lateinit var presenter: SignInPresenter

    @ProvidePresenter
    fun providePresenter() = SignInPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun init() {
        iv_close.setOnClickListener { requireActivity().finish() }
        entrance_registration_button.setOnClickListener { presenter.openRegistrationScreen() }
        entrance_enter_button.setOnClickListener {
            presenter.loginBtnClicked(
                et_sign_in_nickname.text.toString(),
                et_sign_in_password.text.toString()
            )
        }
    }
}