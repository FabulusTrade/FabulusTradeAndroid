package ru.wintrade.ui.fragment.trader.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderRegistrationSecondBinding

class TraderRegistrationFragmentSecond : MvpAppCompatFragment() {
    companion object {
        fun newInstance(): TraderRegistrationFragmentSecond =
            TraderRegistrationFragmentSecond()
    }

    private var _binding: FragmentTraderRegistrationSecondBinding? = null
    private val binding: FragmentTraderRegistrationSecondBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderRegistrationSecondBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}