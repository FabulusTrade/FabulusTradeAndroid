package ru.fabulus.fabulustrade.ui.fragment.entrance

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.inputmask.MaskedTextChangedListener
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentRegistrationAsTraderSecondBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Gender
import ru.fabulus.fabulustrade.mvp.model.entity.SignUpData
import ru.fabulus.fabulustrade.mvp.model.entity.TraderRegistrationInfo
import ru.fabulus.fabulustrade.mvp.presenter.registration.trader.RegAsTraderSecondPresenter
import ru.fabulus.fabulustrade.mvp.view.registration.trader.RegAsTraderSecondView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.REGISTRATION_DATA
import ru.fabulus.fabulustrade.util.TRADER_REG_INFO_TAG
import ru.fabulus.fabulustrade.util.showLongToast
import ru.fabulus.fabulustrade.util.toApiDate
import ru.fabulus.fabulustrade.util.toUiDate


class RegistrationAsTraderFragmentSecond : MvpAppCompatFragment(), RegAsTraderSecondView {
    companion object {
        fun newInstance(): RegistrationAsTraderFragmentSecond =
            RegistrationAsTraderFragmentSecond()

        fun newInstance(signUpData: SignUpData): RegistrationAsTraderFragmentSecond =
            RegistrationAsTraderFragmentSecond().apply {
                arguments = Bundle().apply {
                    putParcelable(REGISTRATION_DATA, signUpData)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: RegAsTraderSecondPresenter

    @ProvidePresenter
    fun providePresenter() = RegAsTraderSecondPresenter(
        arguments?.get(REGISTRATION_DATA) as SignUpData
    ).apply {
        App.instance.appComponent.inject(this)
    }

    private var _binding: FragmentRegistrationAsTraderSecondBinding? = null
    private val binding: FragmentRegistrationAsTraderSecondBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }


    override fun init() {
        initView()
        initListeners()
    }

    override fun setBirthdayError() {
        binding.tiTraderBirthday.run {
            text = null
            error = getString(R.string.error)
        }
    }

    override fun showBirthdayDataPicker(date: Long) {
        val birthDayDialog = MaterialDatePicker.Builder
            .datePicker()
            .setSelection(date)
            .setTitleText(getString(R.string.text_birthday))
            .build()
        birthDayDialog.addOnPositiveButtonClickListener { newDate ->
            presenter.setNewBirthday(newDate)
        }
        birthDayDialog.show(parentFragmentManager, TRADER_REG_INFO_TAG)
    }

    override fun setTraderBirthday(date: String) {
        binding.tiTraderBirthday.setText(date)
    }

    override fun showToast(msg: String) {
        context?.showLongToast(msg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationAsTraderSecondBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    private fun initView() {
        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genders,
            R.layout.support_simple_spinner_dropdown_item
        )
        arguments?.getParcelable<TraderRegistrationInfo>(TRADER_REG_INFO_TAG)?.let { traderInfo ->
            with(binding) {
                tiTraderBirthday.setText(
                    traderInfo.dateOfBirth?.toUiDate()
                )
                tiTraderFirstName.setText(traderInfo.firstName)
                tiTraderPatronymic.setText(traderInfo.patronymic)
                tiTraderLastName.setText(traderInfo.lastName)
                tiTraderGender.setText(traderInfo.gender.text)
            }
        }
        binding.tiTraderGender.setAdapter(genderAdapter)
    }

    private fun initListeners() {
        initDateEditText()
        binding.run {
            btnBackTraderReg2.setOnClickListener {
                presenter.openRegistrationFirstScreen()
            }
            btnForwardTraderReg2.setOnClickListener {
                presenter.saveData(
                    tiTraderBirthday.getTextOrError()?.toApiDate(),
                    tiTraderFirstName.getTextOrError(),
                    tiTraderLastName.getTextOrError(),
                    tiTraderPatronymic.getTextOrError(),
                    tiTraderGender.getTextOrError().let { gender ->
                        Gender.getGender(gender)
                    }
                )
            }
            btnDatePickerTraderReg2.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    presenter.launchBirthdayDataPicker()
                } else {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.enter_date_in_field),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            tiTraderBirthday.run {
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus)
                        presenter.checkBirthday(text.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initDateEditText() {
        MaskedTextChangedListener.installOn(
            editText = binding.tiTraderBirthday,
            primaryFormat = getString(R.string.text_date_format)
        )
    }

    private fun TextView.getTextOrError(): String? =
        if (text.isNullOrBlank()) {
            error = getString(R.string.require_field)
            null
        } else {
            error = null
            text.toString()
        }
}