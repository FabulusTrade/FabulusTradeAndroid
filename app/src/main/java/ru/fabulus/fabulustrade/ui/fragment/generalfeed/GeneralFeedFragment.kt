package ru.fabulus.fabulustrade.ui.fragment.generalfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentGeneralFeedBinding
import ru.fabulus.fabulustrade.mvp.presenter.generalfeed.GeneralFeedPostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.fabulus.fabulustrade.mvp.view.generalfeed.GeneralFeedPostView
import ru.fabulus.fabulustrade.mvp.view.trader.TraderMePostView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.BackButtonListener
import ru.fabulus.fabulustrade.ui.adapter.PostRVAdapter


class GeneralFeedFragment : MvpAppCompatFragment(), GeneralFeedPostView {
    private var _binding: FragmentGeneralFeedBinding? = null
    private val binding: FragmentGeneralFeedBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object{
        fun newInstance() = GeneralFeedFragment()
    }

    @InjectPresenter
    lateinit var presenter: GeneralFeedPostPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMePostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var postRVAdapter: PostRVAdapter? = null

    override fun init() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvGeneralFeed
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralFeedBinding.inflate(inflater, container, false)
        return _binding?.root
    }

}