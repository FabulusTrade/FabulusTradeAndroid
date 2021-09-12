package ru.wintrade.ui.fragment.traderme

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
import ru.wintrade.databinding.FragmentTraderMeSubsTradeBinding
import ru.wintrade.mvp.presenter.traderme.TraderMeSubTradePresenter
import ru.wintrade.mvp.view.traderme.TraderMeSubTradeView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.SubscriberTradesRVAdapter

class TraderMeSubTradeFragment : MvpAppCompatFragment(), TraderMeSubTradeView {
    private var _binding: FragmentTraderMeSubsTradeBinding? = null
    private val binding: FragmentTraderMeSubsTradeBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val POSITION = "position"
        fun newInstance(position: Int) = TraderMeSubTradeFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION, position)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: TraderMeSubTradePresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeSubTradePresenter(
        arguments?.get(POSITION) as Int
    ).apply {
        App.instance.appComponent.inject(this)
    }

    var subscriberTradesRVAdapter: SubscriberTradesRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderMeSubsTradeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init(position: Int) {
        when (position) {
            1 -> presenter.dealsBtnClicked()
            2 -> presenter.ordersBtnClicked()
            3 -> presenter.journalBtnClicked()
        }
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        subscriberTradesRVAdapter = SubscriberTradesRVAdapter(presenter.listPresenter)
        binding.rvTraderMeSubTradeDeal.run {
            adapter = subscriberTradesRVAdapter
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
                }
            )
        }
    }

    override fun setBtnState(state: TraderMeSubTradePresenter.State) {
        when (state) {
            TraderMeSubTradePresenter.State.DEALS -> dealsStateInit()
            TraderMeSubTradePresenter.State.ORDERS -> ordersStateInit()
            TraderMeSubTradePresenter.State.JOURNAL -> journalStateInit()
        }
    }

    override fun updateAdapter() {
        subscriberTradesRVAdapter?.notifyDataSetChanged()
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        binding.layoutTraderMeSubTradeRefresh.isRefreshing = isRefreshing
    }

    fun initListeners() {
        binding.run {
            layoutTraderMeSubTrade.setOnClickListener {
                presenter.refreshed()
            }
            btnTraderMeSubTradeDeal.setOnClickListener {
                presenter.dealsBtnClicked()
            }
            btnTraderMeSubTradeOrders.setOnClickListener {
                presenter.ordersBtnClicked()
            }
            btnTraderMeSubTradeLogs.setOnClickListener {
                presenter.journalBtnClicked()
            }
        }
    }

    private fun journalStateInit() {
        binding.run {
            isActive(btnTraderMeSubTradeLogs)
            isNotActive(btnTraderMeSubTradeDeal)
            isNotActive(btnTraderMeSubTradeOrders)
            layoutTraderMeSubTrade.visibility = View.GONE
            tvTraderMeSubTradeComingSoon.visibility = View.VISIBLE

        }
    }

    private fun ordersStateInit() {
        binding.run {
            isActive(btnTraderMeSubTradeOrders)
            isNotActive(btnTraderMeSubTradeDeal)
            isNotActive(btnTraderMeSubTradeLogs)
            layoutTraderMeSubTrade.visibility = View.GONE
            tvTraderMeSubTradeComingSoon.visibility = View.VISIBLE

        }
    }

    private fun dealsStateInit() {
        binding.run {
            isActive(btnTraderMeSubTradeDeal)
            isNotActive(btnTraderMeSubTradeOrders)
            isNotActive(btnTraderMeSubTradeLogs)
            layoutTraderMeSubTrade.visibility = View.VISIBLE
            tvTraderMeSubTradeComingSoon.visibility = View.GONE

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