package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.button.MaterialButton
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentSubscriberDealBinding
import ru.wintrade.mvp.presenter.subscriber.SubscriberTradePresenter
import ru.wintrade.mvp.presenter.traders.TradersAllPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberDealView
import ru.wintrade.navigation.Screens
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.SubscriberTradesRVAdapter
import javax.inject.Inject

class SubscriberTradeFragment : MvpAppCompatFragment(), SubscriberDealView {
    private var _binding: FragmentSubscriberDealBinding? = null
    private val binding: FragmentSubscriberDealBinding
        get() = checkNotNull(_binding) { R.string.binding_error }

    companion object {
        fun newInstance() = SubscriberTradeFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberTradePresenter

    var subscriberTradesRVAdapter: SubscriberTradesRVAdapter? = null

    @ProvidePresenter
    fun providePresenter() = SubscriberTradePresenter().apply {
        App.instance.appComponent.inject(this)
    }

    @Inject
    lateinit var router: Router

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubscriberDealBinding.inflate(inflater, container, false)
        App.instance.appComponent.inject(this)
        return _binding?.root
    }

    override fun init() {
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        subscriberTradesRVAdapter = SubscriberTradesRVAdapter(presenter.listPresenter)
        binding.rvSubDeal.run {
            adapter = subscriberTradesRVAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val linearLayoutManager = layoutManager as LinearLayoutManager
                            val visibleItemCount = linearLayoutManager.childCount
                            val totalItemCount = linearLayoutManager.itemCount
                            val pastVisiblesItems =
                                linearLayoutManager.findFirstVisibleItemPosition()
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                presenter.onScrollLimit()
                            }

                        }
                    }
                }
            )
        }
    }

    fun initListeners() {
        with(binding) {
            layoutSubDealRefresh.setOnRefreshListener {
                presenter.refreshed()
            }
            btnSubDealTrades.setOnClickListener {
                presenter.dealsBtnClicked()
            }
            btnSubDealOrders.setOnClickListener {
                presenter.ordersBtnClicked()
            }
            btnSubDealLogs.setOnClickListener {
                presenter.journalBtnClicked()
            }
            layoutHasNoSubs.tvChooseSubscribe.setOnClickListener {
                router.navigateTo(Screens.tradersAllScreen(TradersAllPresenter.DEFAULT_FILTER))
            }
        }
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        binding.layoutSubDealRefresh.isRefreshing = isRefreshing
    }

    override fun withoutSubscribeAnyTrader() {
        binding.layoutHasNoSubs.root.visibility = View.VISIBLE
    }

    override fun updateAdapter() {
        subscriberTradesRVAdapter?.notifyDataSetChanged()
    }


    override fun setBtnState(state: SubscriberTradePresenter.State) {
        when (state) {
            SubscriberTradePresenter.State.DEALS -> {
                dealsStateInit()
            }
            SubscriberTradePresenter.State.ORDERS -> {
                ordersStateInit()
            }
            SubscriberTradePresenter.State.JOURNAL -> {
                journalStateInit()
            }
        }
    }

    private fun dealsStateInit() {
        isActive(binding.btnSubDealTrades)
        isNotActive(binding.btnSubDealOrders)
        isNotActive(binding.btnSubDealLogs)
        binding.layoutSubDealRefresh.visibility = View.VISIBLE
        binding.tvSubDealComingSoon.visibility = View.GONE
    }

    private fun ordersStateInit() {
        isNotActive(binding.btnSubDealTrades)
        isActive(binding.btnSubDealOrders)
        isNotActive(binding.btnSubDealLogs)
        binding.layoutSubDealRefresh.visibility = View.GONE
        binding.tvSubDealComingSoon.visibility = View.VISIBLE
    }

    private fun journalStateInit() {
        isNotActive(binding.btnSubDealTrades)
        isNotActive(binding.btnSubDealOrders)
        isActive(binding.btnSubDealLogs)
        binding.layoutSubDealRefresh.visibility = View.GONE
        binding.tvSubDealComingSoon.visibility = View.VISIBLE
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