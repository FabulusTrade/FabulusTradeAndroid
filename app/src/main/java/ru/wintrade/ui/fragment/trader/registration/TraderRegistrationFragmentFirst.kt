package ru.wintrade.ui.fragment.trader.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderRegistrationFirstBinding

class TraderRegistrationFragmentFirst : MvpAppCompatFragment() {
    companion object {
        fun newInstance(): TraderRegistrationFragmentFirst =
            TraderRegistrationFragmentFirst()
    }


    private var _binding: FragmentTraderRegistrationFirstBinding? = null
    private val binding: FragmentTraderRegistrationFirstBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderRegistrationFirstBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}