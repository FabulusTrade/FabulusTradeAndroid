package ru.wintrade.ui.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.jakewharton.rxbinding4.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.layout_title.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.SignUpPresenter
import ru.wintrade.mvp.view.SignUpView
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.MainActivity
import java.util.concurrent.TimeUnit

class SignUpFragment: MvpAppCompatFragment(), SignUpView {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    @ProvidePresenter
    fun providePresenter() = SignUpPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_up, container, false)

    override fun init() {
        tv_sign_privacy.movementMethod = LinkMovementMethod.getInstance()
        tv_sign_rules.movementMethod = LinkMovementMethod.getInstance()
        initListeners()
        iv_close.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initListeners() {
        cb_sign_privacy.setOnCheckedChangeListener { compoundButton, b ->
            presenter.privacyCheckChanged(b)
        }

        cb_sign_rules.setOnCheckedChangeListener { compoundButton, b ->
            presenter.rulesCheckChanged(b)
        }

        et_sign_nickname.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribe(
                {
                    presenter.nicknameChanged(it.toString())
                },
                {}
            )

        et_sign_email.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribe(
                {
                    presenter.emailChanged(it.toString())
                },
                {}
            )

        et_sign_password.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribe(
                {
                    presenter.passwordChanged(it.toString())
                },
                {}
            )

        et_sign_confirm_password.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribe(
                {
                    presenter.confirmPasswordChanged(it.toString())
                },
                {}
            )

        et_sign_phone.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribe(
                {
                    presenter.phoneChanged(it.toString())
                },
                {}
            )

        btn_sign_create.setOnClickListener {
            presenter.createProfileClicked()

        }
    }

}