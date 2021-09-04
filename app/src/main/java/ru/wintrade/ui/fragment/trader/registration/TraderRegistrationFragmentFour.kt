package ru.wintrade.ui.fragment.trader.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderRegistrationFourBinding

class TraderRegistrationFragmentFour : Fragment() {
    companion object {
        fun newInstance(): TraderRegistrationFragmentFour =
            TraderRegistrationFragmentFour()
    }

    private var _binding: FragmentTraderRegistrationFourBinding? = null
    private val binding: FragmentTraderRegistrationFourBinding
        get() = checkNotNull(_binding) { getString(R.string.error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderRegistrationFourBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}