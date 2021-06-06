package ru.wintrade.ui.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.layout_title.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.SignUpPresenter
import ru.wintrade.mvp.view.SignUpView
import ru.wintrade.ui.App
import ru.wintrade.util.EmailValidation
import ru.wintrade.util.NicknameValidation
import ru.wintrade.util.PasswordValidation
import ru.wintrade.util.PhoneValidation


class SignUpFragment : MvpAppCompatFragment(), SignUpView {

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
        setDrawerLockMode()
        tv_sign_privacy.movementMethod = LinkMovementMethod.getInstance()
        tv_sign_rules.movementMethod = LinkMovementMethod.getInstance()
        initListeners()
    }

    private fun setDrawerLockMode() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        requireActivity().toolbar_blue.visibility = View.GONE
    }

    override fun showRegulationsAcceptToast() {
        Toast.makeText(context, R.string.regulations_accept, Toast.LENGTH_LONG).show()
    }

    override fun showSuccessToast() {
        Toast.makeText(context, R.string.is_success_registration, Toast.LENGTH_LONG).show()
    }

    override fun setNicknameError(validation: NicknameValidation) {
        et_sign_nickname_layout.error =
            when (validation) {
                NicknameValidation.TOO_SHORT -> getString(R.string.nickname_too_short)
                NicknameValidation.TOO_LONG -> getString(R.string.nickname_too_long)
                NicknameValidation.ONLY_ENG_AND_DIGIT -> getString(R.string.nickname_only_eng_and_digits)
                NicknameValidation.ALREADY_EXISTS -> getString(R.string.nickname_already_exists)
                NicknameValidation.OK -> null
            }
    }

    override fun setPasswordError(validation: PasswordValidation) {
        et_sign_password_layout.error =
            when (validation) {
                PasswordValidation.TOO_SHORT -> getString(R.string.password_too_short)
                PasswordValidation.NO_UPPERCASE_OR_LOWERCASE_OR_DIGIT -> getString(R.string.password_no_up_low_case_digits)
                PasswordValidation.OK -> null
            }

    }

    override fun setPasswordConfirmError(isCorrect: Boolean) {
        if (isCorrect)
            et_sign_confirm_password_layout.error = null
        else
            et_sign_confirm_password_layout.error = getString(R.string.password_mismatch)
    }

    override fun setEmailError(validation: EmailValidation) {
        et_sign_email_layout.error = when (validation) {
            EmailValidation.INCORRECT -> getString(R.string.email_incorrect)
            EmailValidation.ALREADY_EXISTS -> getString(R.string.email_already_exists)
            EmailValidation.OK -> null
        }
    }

    override fun setPhoneError(validation: PhoneValidation) {
        et_sign_phone_layout.error = when (validation) {
            PhoneValidation.INCORRECT -> getString(R.string.phone_incorrect)
            PhoneValidation.ALREADY_EXISTS -> getString(R.string.phone_already_exists)
            PhoneValidation.OK -> null
        }
    }

    private fun initListeners() {
        initPhoneEditText()

        et_sign_nickname.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                presenter.onNicknameInputFocusLost(et_sign_nickname.text.toString())
        }

        et_sign_password.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                presenter.onPasswordInputFocusLost(et_sign_password.text.toString())
        }

        et_sign_confirm_password.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                presenter.onConfirmPasswordInputFocusLost(et_sign_confirm_password.text.toString())
        }

        et_sign_phone.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                presenter.onPhoneInputFocusLost()
        }

        et_sign_email.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                presenter.onEmailInputFocusLost(et_sign_email.text.toString())
        }

        btn_sign_create.setOnClickListener {
            presenter.createProfileClicked(
                et_sign_nickname.text.toString(),
                et_sign_password.text.toString(),
                et_sign_confirm_password.text.toString(),
                et_sign_email.text.toString(),
                cb_sign_rules.isChecked,
                cb_sign_privacy.isChecked
            )
        }
    }

    private fun initPhoneEditText() {
        val affineFormats: MutableList<String> = ArrayList()
        affineFormats.add("8 ([000]) [000]-[00]-[00]")

        val listener: MaskedTextChangedListener = installOn(
            et_sign_phone,
            "+7 ([000]) [000]-[00]-[00]",
            affineFormats, AffinityCalculationStrategy.PREFIX,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedText: String
                ) {
                    presenter.onPhoneChanged(extractedValue, maskFilled)
                }
            }
        )

        et_sign_phone.hint = listener.placeholder()
    }
}