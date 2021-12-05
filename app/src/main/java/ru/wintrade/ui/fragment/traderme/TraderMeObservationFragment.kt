package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderMeObservationBinding
import ru.wintrade.mvp.presenter.traderme.TraderMeObservationPresenter
import ru.wintrade.mvp.view.traderme.TraderMeObservationView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.ObservationRVAdapter
import ru.wintrade.util.showToast


class TraderMeObservationFragment : MvpAppCompatFragment(), TraderMeObservationView {
    private var _binding: FragmentTraderMeObservationBinding? = null
    private val binding: FragmentTraderMeObservationBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = TraderMeObservationFragment()
        const val DEALS = 1
        const val ORDERS = 2
        const val JOURNALS = 3
    }

    @InjectPresenter
    lateinit var presenter: TraderMeObservationPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeObservationPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var observationRVAdapter: ObservationRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderMeObservationBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initAdapter()
        initListeners()
    }

    fun initAdapter() {
        observationRVAdapter = ObservationRVAdapter(presenter.listPresenter).apply {
            App.instance.appComponent.inject(this)
        }
        binding.rvTraderMeSub.run {
            adapter = observationRVAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun initListeners() {
        binding.run {
            btnTraderMeSubDealTrades.setOnClickListener {
                presenter.openTraderMeSubScreen(DEALS)
            }
            btnTraderMeSubDealOrders.setOnClickListener {
                presenter.openTraderMeSubScreen(ORDERS)
            }
            btnTraderMeSubDealLogs.setOnClickListener {
                presenter.openTraderMeSubScreen(JOURNALS)
            }
        }
    }

    override fun updateAdapter() {
        observationRVAdapter?.notifyDataSetChanged()
    }

    override fun showToast(text: String, duration: Int) {
        requireContext().showToast(text, duration)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}