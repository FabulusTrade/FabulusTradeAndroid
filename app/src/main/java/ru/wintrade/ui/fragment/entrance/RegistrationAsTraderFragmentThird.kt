package ru.wintrade.ui.fragment.entrance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import ru.wintrade.R
import ru.wintrade.databinding.FragmentRegistrationAsTraderThirdBinding

class RegistrationAsTraderFragmentThird : MvpAppCompatFragment() {
    companion object {
        fun newInstance(): RegistrationAsTraderFragmentThird =
            RegistrationAsTraderFragmentThird()
    }

    private var _binding: FragmentRegistrationAsTraderThirdBinding? = null
    private val binding: FragmentRegistrationAsTraderThirdBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

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