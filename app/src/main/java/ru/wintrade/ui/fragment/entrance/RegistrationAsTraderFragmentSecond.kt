package ru.wintrade.ui.fragment.entrance

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
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
                    traderInfo.dateOfBirth?.split("-")?.reversed()?.joinToString(".")
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
                    val datePicker = DatePickerDialog(requireContext())
                    datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                        val dateText =
                            String.format(DATE_FORMAT_STRING, dayOfMonth, month.plus(1), year)
                        binding.tiTraderBirthday.setText(dateText)
                    }
                    datePicker.show()
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

    private fun saveTraderInfo(): TraderRegistrationInfo? {
        return binding.run {
            tiTraderFirstName.getTextOrError()?.let { fistName ->
                tiTraderPatronymic.getTextOrError()?.let { patronymic ->
                    tiTraderLastName.getTextOrError()?.let { lastName ->
                        tiTraderGender.getTextOrError()?.let { gender ->
                            tiTraderBirthday.getTextOrError()?.let { birthDay ->
                                TraderRegistrationInfo(
                                    birthDay.split(".").reversed().joinToString("-"),
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

    fun TextView.getTextOrError(): String? =
        if (text.isNullOrBlank()) {
            error = getString(R.string.require_field)
            null
        } else {
            error = null
            text.toString()
        }
}