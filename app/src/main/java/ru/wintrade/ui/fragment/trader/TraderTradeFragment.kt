package ru.wintrade.ui.fragment.trader

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
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderTradeBinding
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.trader.TraderTradePresenter
import ru.wintrade.mvp.view.trader.TraderDealView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TradesByCompanyRVAdapter

class TraderTradeFragment : MvpAppCompatFragment(), TraderDealView {
    private var _binding: FragmentTraderTradeBinding? = null
    private val binding: FragmentTraderTradeBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        const val TRADER = "trader"
        fun newInstance(trader: Trader) =
            TraderTradeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TRADER, trader)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: TraderTradePresenter

    @ProvidePresenter
    fun providePresenter() = TraderTradePresenter(
        arguments?.get(TRADER) as Trader
    ).apply {
        App.instance.appComponent.inject(this)
    }

    private var adapter: TradesByCompanyRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderTradeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        adapter = TradesByCompanyRVAdapter(presenter.listPresenter)
        binding.run {
            rvTraderTradeAggregated.adapter = adapter
            rvTraderTradeAggregated.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addOnScrollListener(
                    object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            if (dy > 0) {
                                val layoutManager = layoutManager as LinearLayoutManager
                                val visibleItemCount = layoutManager.childCount
                                val totalItemCount = layoutManager.itemCount
                                val pastVisiblesItems =
                                    layoutManager.findFirstVisibleItemPosition()
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    presenter.onScrollLimit()
                                }
                            }
                        }
                    })
            }
        }
    }

    fun initListeners() {
        binding.run {
            btnTraderTradeDeals.setOnClickListener {
                presenter.myDealsBtnClicked()
            }
            btnTraderTradeOrders.setOnClickListener {
                presenter.myOrdersBtnClicked()
            }
            btnTraderTradeJournal.setOnClickListener {
                presenter.myJournalBtnClicked()
            }
            btnTraderTradeEntry.setOnClickListener {
                presenter.openSignInScreen()
            }
            btnTraderTradeRegistration.setOnClickListener {
                presenter.openSignUpScreen()
            }
        }
    }

    override fun setBtnState(state: TraderTradePresenter.State) {
        when (state) {
            TraderTradePresenter.State.MY_DEALS -> {
                myDealsStateInit()
            }
            TraderTradePresenter.State.MY_ORDERS -> {
                myOrdersStateInit()
            }
            TraderTradePresenter.State.MY_JOURNAL -> {
                myJournalStateInit()
            }
        }
    }

    private fun myJournalStateInit() {
        with(binding) {
            isActive(btnTraderTradeJournal)
            isNotActive(btnTraderTradeDeals)
            isNotActive(btnTraderTradeOrders)
            rvTraderTradeDeals.visibility = View.GONE
            rvTraderTradeOrders.visibility = View.GONE
            rvTraderTradeJournal.visibility = View.VISIBLE
            tvTraderDealMonthlyCounter.visibility = View.GONE
        }
    }

    private fun myOrdersStateInit() {
        with(binding) {
            isActive(btnTraderTradeOrders)
            isNotActive(btnTraderTradeDeals)
            isNotActive(btnTraderTradeJournal)
            rvTraderTradeDeals.visibility = View.GONE
            rvTraderTradeOrders.visibility = View.VISIBLE
            rvTraderTradeJournal.visibility = View.GONE
            tvTraderDealMonthlyCounter.visibility = View.GONE
        }
    }

    private fun myDealsStateInit() {
        with(binding) {
            isActive(btnTraderTradeDeals)
            isNotActive(btnTraderTradeOrders)
            isNotActive(btnTraderTradeJournal)
            rvTraderTradeDeals.visibility = View.VISIBLE
            rvTraderTradeOrders.visibility = View.GONE
            rvTraderTradeJournal.visibility = View.GONE
            tvTraderDealMonthlyCounter.visibility = View.VISIBLE
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

    override fun updateRecyclerView() {
        adapter?.notifyDataSetChanged()
    }

    override fun isAuthorized(isAuth: Boolean) {
        with(binding) {
            if (isAuth) {
                layoutTraderTradeIsAuth.visibility = View.VISIBLE
                layoutTraderTradeNotAuth.visibility = View.GONE
            } else {
                layoutTraderTradeIsAuth.visibility = View.GONE
                layoutTraderTradeNotAuth.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}