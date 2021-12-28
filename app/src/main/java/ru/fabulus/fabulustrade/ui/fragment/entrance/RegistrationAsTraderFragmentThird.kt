package ru.fabulus.fabulustrade.ui.fragment.entrance

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentRegistrationAsTraderThirdBinding
import ru.fabulus.fabulustrade.mvp.model.entity.SignUpData
import ru.fabulus.fabulustrade.mvp.presenter.registration.trader.RegAsTraderThirdPresenter
import ru.fabulus.fabulustrade.mvp.view.registration.trader.RegAsTraderThirdView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.REGISTRATION_DATA
import ru.fabulus.fabulustrade.util.setToolbarVisible

class RegistrationAsTraderFragmentThird : MvpAppCompatFragment(), RegAsTraderThirdView {
    companion object {
        fun newInstance(signUpData: SignUpData): RegistrationAsTraderFragmentThird =
            RegistrationAsTraderFragmentThird().apply {
                arguments = Bundle().apply {
                    putParcelable(REGISTRATION_DATA, signUpData)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: RegAsTraderThirdPresenter
    lateinit var terminalAdapter: ArrayAdapter<CharSequence>

    @ProvidePresenter
    fun providePresenter() = RegAsTraderThirdPresenter(
        arguments?.get(REGISTRATION_DATA) as SignUpData
    ).apply {
        App.instance.appComponent.inject(this)
    }

    private var _binding: FragmentRegistrationAsTraderThirdBinding? = null
    private val binding: FragmentRegistrationAsTraderThirdBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun init() {
        initView()
        initListeners()
    }

    private fun initView() {
        terminalAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.terminals,
            R.layout.support_simple_spinner_dropdown_item
        )
        binding.tiTradeTerminal.run {
            setText(terminalAdapter.getItem(0))
            setAdapter(terminalAdapter)
        }
    }

    override fun showSuccessfulPatchData() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.trader_reg_patch_dialog_message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                setToolbarVisible(true)
                presenter.openNextStageScreen()
            }.show()
    }

    override fun showErrorPatchData(e: Throwable) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.trader_reg_error_dialog_message, e.message))
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun renderInstructionText(text: String) {
        binding.tvTraderReg4Text1.text = text
    }

    override fun showSelectedBrokerDialog(msg: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(msg)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                initView()
            }.show()
    }

    private fun initListeners() {
        with(binding) {
            btnBackTraderReg3.setOnClickListener {
                presenter.openRegistrationSecondScreen()
            }
            btnReadyTraderReg3.setOnClickListener {
                presenter.saveTraderRegistrationInfo()
            }
            tiTradeTerminal.setOnItemClickListener { adapterView, _, position, _ ->
                when (position) {
                    0 -> return@setOnItemClickListener
                    else -> presenter.onNotTinkoffBrokerClicked(
                        adapterView.adapter.getItem(position).toString()
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationAsTraderThirdBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}