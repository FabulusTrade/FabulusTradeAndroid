package ru.wintrade.ui.fragment.entrance

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentRegistrationAsTraderSecondBinding
import ru.wintrade.mvp.model.entity.Gender
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.presenter.registration.trader.RegAsTraderSecondPresenter
import ru.wintrade.mvp.view.registration.trader.RegAsTraderSecondView
import ru.wintrade.ui.App
import ru.wintrade.util.TRADER_REG_INFO_TAG
import java.util.*

private const val DATE_FORMAT_STRING = "%02d.%02d.%04d"

class RegistrationAsTraderFragmentSecond : MvpAppCompatFragment(), RegAsTraderSecondView {
    companion object {
        fun newInstance(): RegistrationAsTraderFragmentSecond =
            RegistrationAsTraderFragmentSecond()

        fun newInstance(traderInfo: TraderRegistrationInfo): RegistrationAsTraderFragmentSecond =
            RegistrationAsTraderFragmentSecond().apply {
                arguments = Bundle().apply {
                    putParcelable(TRADER_REG_INFO_TAG, traderInfo)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: RegAsTraderSecondPresenter

    @ProvidePresenter
    fun providePresenter() = RegAsTraderSecondPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var _binding: FragmentRegistrationAsTraderSecondBinding? = null
    private val binding: FragmentRegistrationAsTraderSecondBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }


    override fun init() {
        initView()
        initListeners()
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
        binding.run {
            btnBackTraderReg2.setOnClickListener {
                presenter.openRegistrationFirstScreen()
            }
            btnForwardTraderReg2.setOnClickListener {
                saveTraderInfo()?.let {
                    presenter.openRegistrationThirdScreen(it)
                }
            }
            btnDatePickerTraderReg2.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    showBirthDayDialog()
                } else {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.enter_date_in_field),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showBirthDayDialog() {
        val selectDate = if (binding.tiTraderBirthday.text.isNullOrBlank()) {
            Date().time
        } else {
            binding.tiTraderBirthday.tag.toString().toLong()
        }
        val birthDayDialog = MaterialDatePicker.Builder
            .datePicker()
            .setSelection(selectDate)
            .setTitleText(getString(R.string.text_birthday))
            .build()
        birthDayDialog.addOnPositiveButtonClickListener { newDate ->
            Calendar.getInstance().apply {
                time = Date(newDate)
                binding.tiTraderBirthday.tag = time.time.toString()
                binding.tiTraderBirthday.setText(
                    String.format(
                        DATE_FORMAT_STRING,
                        this[Calendar.DAY_OF_MONTH],
                        this[Calendar.MONTH].plus(1),
                        this[Calendar.YEAR]
                    )
                )
            }
        }
        birthDayDialog.show(parentFragmentManager, TRADER_REG_INFO_TAG)
    }

    private fun saveTraderInfo(): TraderRegistrationInfo? {
        return binding.run {
            tiTraderFirstName.getTextOrError()?.let { fistName ->
                tiTraderPatronymic.getTextOrError()?.let { patronymic ->
                    tiTraderLastName.getTextOrError()?.let { lastName ->
                        tiTraderGender.getTextOrError()?.let { gender ->
                            tiTraderBirthday.getTextOrError()?.let { birthDay ->
                                TraderRegistrationInfo(
                                    birthDay.toApiDate(),
                                    fistName,
                                    lastName,
                                    patronymic,
                                    Gender.getGender(gender)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun TextView.getTextOrError(): String? =
        if (text.isNullOrBlank()) {
            error = getString(R.string.require_field)
            null
        } else {
            error = null
            text.toString()
        }

    private fun String.toApiDate(): String =
        split(".").reversed().joinToString("-")

    private fun String.toUiDate(): String =
        split("-").reversed().joinToString(".")
}