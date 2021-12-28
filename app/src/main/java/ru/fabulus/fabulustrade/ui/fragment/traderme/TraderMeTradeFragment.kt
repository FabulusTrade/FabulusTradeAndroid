package ru.fabulus.fabulustrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTraderMeTradeBinding
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeTradePresenter
import ru.fabulus.fabulustrade.mvp.view.traderme.TraderMeTradeView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.TradesByCompanyRVAdapter

class TraderMeTradeFragment : MvpAppCompatFragment(), TraderMeTradeView {
    private var _binding: FragmentTraderMeTradeBinding? = null
    private val binding: FragmentTraderMeTradeBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = TraderMeTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMeTradePresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var tradesByCompanyRVAdapter: TradesByCompanyRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderMeTradeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initListeners()
        initRV()
    }

    fun initListeners() {
        binding.run {
            btnTraderMeTradeDeals.setOnClickListener {
                presenter.myDealsBtnClicked()
            }
            btnTraderMeTradeOrders.setOnClickListener {
                presenter.myOrdersBtnClicked()
            }
            btnTraderMeTradeJournal.setOnClickListener {
                presenter.myJournalBtnClicked()
            }
        }
    }

    private fun initRV() {
        tradesByCompanyRVAdapter = TradesByCompanyRVAdapter(presenter.listPresenter)
        binding.rvTraderMeTradeDeals.run {
            adapter = tradesByCompanyRVAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val layoutManager = layoutManager as LinearLayoutManager
                            val visibleItemCount = layoutManager.childCount
                            val totalItemCount = layoutManager.itemCount
                            val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                presenter.onScrollLimit()
                            }
                        }
                    }
                })
        }
    }

    override fun setBtnState(state: TraderMeTradePresenter.State) {
        when (state) {
            TraderMeTradePresenter.State.MY_DEALS -> {
                myDealsStateInit()
            }
            TraderMeTradePresenter.State.MY_ORDERS -> {
                myOrdersStateInit()
            }
            TraderMeTradePresenter.State.MY_JOURNAL -> {
                myJournalStateInit()
            }
        }
    }

    override fun updateTradesAdapter() {
        tradesByCompanyRVAdapter?.notifyDataSetChanged()
    }

    override fun renderOperationsCount(operationsCount: Int) {
        binding.tvTraderMeDealMonthlyCounter.append(" $operationsCount")
    }

    private fun myJournalStateInit() {
        binding.run {
            isActive(btnTraderMeTradeJournal)
            isNotActive(btnTraderMeTradeDeals)
            isNotActive(btnTraderMeTradeOrders)
            rvTraderMeTradeDeals.visibility = View.GONE
            rvTraderMeTradeOrders.visibility = View.GONE
            rvTraderMeTradeJournal.visibility = View.VISIBLE
            tvTraderMeDealMonthlyCounter.visibility = View.GONE
        }
    }

    private fun myOrdersStateInit() {
        binding.run {
            isActive(btnTraderMeTradeOrders)
            isNotActive(btnTraderMeTradeDeals)
            isNotActive(btnTraderMeTradeJournal)
            rvTraderMeTradeDeals.visibility = View.GONE
            rvTraderMeTradeOrders.visibility = View.VISIBLE
            rvTraderMeTradeJournal.visibility = View.GONE
            tvTraderMeDealMonthlyCounter.visibility = View.GONE
        }
    }

    private fun myDealsStateInit() {
        binding.run {
            isActive(btnTraderMeTradeDeals)
            isNotActive(btnTraderMeTradeOrders)
            isNotActive(btnTraderMeTradeJournal)
            rvTraderMeTradeDeals.visibility = View.VISIBLE
            rvTraderMeTradeOrders.visibility = View.GONE
            rvTraderMeTradeJournal.visibility = View.GONE
            tvTraderMeDealMonthlyCounter.visibility = View.VISIBLE
        }
    }

    private fun isActive(activeBtn: MaterialButton) {
        activeBtn.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorLightGreen)
            setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary))
        }
    }

    private fun isNotActive(inactiveBtn: MaterialButton) {
        inactiveBtn.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)
            setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorGray))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}