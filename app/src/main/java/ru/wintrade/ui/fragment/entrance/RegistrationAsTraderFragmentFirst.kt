package ru.wintrade.ui.fragment.entrance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import ru.wintrade.R
import ru.wintrade.databinding.FragmentRegistrationAsTraderFirstBinding

class RegistrationAsTraderFragmentFirst : MvpAppCompatFragment() {
    companion object {
        fun newInstance(): RegistrationAsTraderFragmentFirst =
            RegistrationAsTraderFragmentFirst()
    }

    private var _binding: FragmentRegistrationAsTraderFirstBinding? = null
    private val binding: FragmentRegistrationAsTraderFirstBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationAsTraderFirstBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}