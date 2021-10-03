package ru.wintrade.ui.fragment.entrance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import ru.wintrade.R
import ru.wintrade.databinding.FragmentRegistrationAsTraderFourBinding

class RegistrationAsTraderFragmentFour : MvpAppCompatFragment() {
    companion object {
        fun newInstance(): RegistrationAsTraderFragmentFour =
            RegistrationAsTraderFragmentFour()
    }

    private var _binding: FragmentRegistrationAsTraderFourBinding? = null
    private val binding: FragmentRegistrationAsTraderFourBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationAsTraderFourBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}