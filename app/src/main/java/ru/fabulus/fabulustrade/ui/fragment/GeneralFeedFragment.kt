package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentCreatePostBinding
import ru.fabulus.fabulustrade.databinding.FragmentGeneralFeedBinding


class GeneralFeedFragment : Fragment() {

    companion object{
        fun newInstance() = GeneralFeedFragment()
    }

    private var _binding: FragmentGeneralFeedBinding? = null

    private val binding: FragmentGeneralFeedBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralFeedBinding.inflate(inflater, container, false)
        return _binding?.root
    }

}