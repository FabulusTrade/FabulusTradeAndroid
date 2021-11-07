package ru.wintrade.ui.fragment.entrance

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentSignUpBinding
import ru.wintrade.mvp.model.entity.SignUpData
import ru.wintrade.mvp.presenter.registration.subscriber.SignUpPresenter
import ru.wintrade.mvp.view.registration.subscriber.SignUpView
import ru.wintrade.ui.App
import ru.wintrade.util.*


class SignUpFragment : MvpAppCompatFragment(), SignUpView {
    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val IS_AS_TRADER_REGISTRATION = "registration"
        fun newInstance(asTraderRegistration: Boolean) = SignUpFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_AS_TRADER_REGISTRATION, asTraderRegistration)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    @ProvidePresenter
    fun providePresenter() = SignUpPresenter(
        arguments?.get(IS_AS_TRADER_REGISTRATION) as Boolean
    ).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initView()
        binding.tvSignPrivacy.movementMethod = LinkMovementMethod.getInstance()
        binding.tvSignRules.movementMethod = LinkMovementMethod.getInstance()
        initListeners()
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setToolbarVisible(false)
    }

    override fun setNicknameError(validation: NicknameValidation) {
        binding.etSignNicknameLayout.error =
            when (validation) {
                NicknameValidation.TOO_SHORT -> getString(R.string.nickname_too_short)
                NicknameValidation.TOO_LONG -> getString(R.string.nickname_too_long)
                NicknameValidation.ONLY_ENG_AND_DIGIT -> getString(R.string.nickname_only_eng_and_digits)
                NicknameValidation.ALREADY_EXISTS -> getString(R.string.nickname_already_exists)
                NicknameValidation.OK -> null
            }
    }

    override fun setPasswordError(validation: PasswordValidation) {
        binding.etSignPasswordLayout.error =
            when (validation) {
                PasswordValidation.TOO_SHORT -> getString(R.string.password_too_short)
                PasswordValidation.NO_UPPERCASE_OR_LOWERCASE_OR_DIGIT -> getString(R.string.password_no_up_low_case_digits)
                PasswordValidation.OK -> null
            }

    }

    override fun setPasswordConfirmError(isCorrect: Boolean) {
        binding.etSignConfirmPasswordLayout
            .error = if (isCorrect)
            null
        else
            getString(R.string.password_mismatch)

    }

    override fun setEmailError(validation: EmailValidation) {
        binding.etSignEmailLayout.error = when (validation) {
            EmailValidation.INCORRECT -> getString(R.string.email_incorrect_signUp)
            EmailValidation.ALREADY_EXISTS -> getString(R.string.email_already_exists)
            EmailValidation.OK -> null
        }
    }

    override fun setPhoneError(validation: PhoneValidation) {
        binding.etSignPhoneLayout.error = when (validation) {
            PhoneValidation.INCORRECT -> getString(R.string.phone_incorrect)
            PhoneValidation.ALREADY_EXISTS -> getString(R.string.phone_already_exists)
            PhoneValidation.OK -> null
        }
    }

    override fun showDialog(title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.ok_signUp) { _, _ ->
            }.show()
    }

    private fun initListeners() {
        initPhoneEditText()

        binding.etSignNickname.run {
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    presenter.onNicknameInputFocusLost(text.toString())
            }
        }

        binding.etSignPassword.run {
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    presenter.onPasswordInputFocusLost(text.toString())
            }
        }

        binding.etSignConfirmPassword.run {
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    presenter.onConfirmPasswordInputFocusLost(text.toString())
            }
        }

        binding.etSignPhone.run {
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    presenter.onPhoneInputFocusLost()
            }
        }

        binding.etSignEmail.run {
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    presenter.onEmailInputFocusLost(text.toString())
            }
        }

        binding.btnSignCreate.setOnClickListener {
            with(binding) {
                presenter.createProfileClicked(
                    etSignNickname.text.toString(),
                    etSignPassword.text.toString(),
                    etSignConfirmPassword.text.toString(),
                    etSignEmail.text.toString(),
                    cbSignRules.isChecked,
                    cbSignPrivacy.isChecked
                )
            }
        }

        binding.tvSignPrivacy.setOnClickListener {
            presenter.openRules()
        }
    }

    private fun initPhoneEditText() {
        val affineFormats: MutableList<String> = ArrayList()
        affineFormats.add(getString(R.string.phone_format_1))

        val listener: MaskedTextChangedListener = installOn(
            binding.etSignPhone,
            getString(R.string.phone_format_2),
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
        binding.etSignPhone.hint = listener.placeholder()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}