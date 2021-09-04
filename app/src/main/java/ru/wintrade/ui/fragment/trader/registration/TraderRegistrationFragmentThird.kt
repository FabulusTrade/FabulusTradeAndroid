package ru.wintrade.ui.fragment.trader.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderRegistrationThirdBinding

class TraderRegistrationFragmentThird : Fragment() {
    companion object {
        fun newInstanse(): TraderRegistrationFragmentThird =
            TraderRegistrationFragmentThird()
    }

    private var _binding: FragmentTraderRegistrationThirdBinding? = null
    private val binding: FragmentTraderRegistrationThirdBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderRegistrationThirdBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}