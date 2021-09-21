package ru.wintrade.ui.fragment.entrance

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentRegistrationAsTraderThirdBinding
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.presenter.registration.trader.RegAsTraderThirdPresenter
import ru.wintrade.mvp.view.registration.trader.RegAsTraderThirdView
import ru.wintrade.ui.App
import ru.wintrade.util.TRADER_REG_INFO_TAG

class RegistrationAsTraderFragmentThird : MvpAppCompatFragment(), RegAsTraderThirdView {
    companion object {
        fun newInstance(traderInfo: TraderRegistrationInfo): RegistrationAsTraderFragmentThird =
            RegistrationAsTraderFragmentThird().apply {
                arguments = Bundle().apply {
                    putParcelable(TRADER_REG_INFO_TAG, traderInfo)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: RegAsTraderThirdPresenter

    @ProvidePresenter
    fun providePresenter() = RegAsTraderThirdPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var _binding: FragmentRegistrationAsTraderThirdBinding? = null
    private val binding: FragmentRegistrationAsTraderThirdBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    private var traderInfo: TraderRegistrationInfo? = null

    override fun init() {
        initListeners()
        arguments?.getParcelable<TraderRegistrationInfo>(TRADER_REG_INFO_TAG)?.let {
            traderInfo = it
        }
    }

    override fun showSuccessfulPatchData() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.trader_reg_patch_dialog_title)
            .setMessage(R.string.trader_reg_patch_dialog_message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                presenter.openNextStageScreen()
            }.show()
    }

    override fun showErrorPatchData(e: Throwable) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setMessage(getString(R.string.trader_reg_error_dialog_message, e.message))
            .setIcon(R.drawable.ic_profit_info)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun initListeners() {
        binding.btnBackTraderReg3.setOnClickListener {
            traderInfo?.let { traderInfo -> presenter.openRegistrationSecondScreen(traderInfo) }
        }
        binding.btnReadyTraderReg3.setOnClickListener {
            traderInfo?.let { presenter.saveTraderRegistrationInfo(it) }
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