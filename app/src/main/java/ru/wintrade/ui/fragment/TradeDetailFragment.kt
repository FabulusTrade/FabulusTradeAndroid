package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTradeDetailBinding
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.presenter.TradeDetailPresenter
import ru.wintrade.mvp.view.TradeDetailView
import ru.wintrade.ui.App

class TradeDetailFragment : MvpAppCompatFragment(), TradeDetailView {
    private var _binding: FragmentTradeDetailBinding? = null
    private val binding: FragmentTradeDetailBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        const val TRADE_KEY = "trade"
        fun newInstance(trade: Trade) = TradeDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TRADE_KEY, trade)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: TradeDetailPresenter

    @ProvidePresenter
    fun providePresenter() = TradeDetailPresenter(requireArguments()[TRADE_KEY] as Trade).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradeDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        binding.ivTradeDetailClose.setOnClickListener {
            presenter.closeClicked()
        }
    }

    override fun setName(traderName: String) {
        binding.tvTradeDetailTraderName.text = traderName
    }

    override fun setType(type: String) {
        binding.tvTradeDetailOperation.text = type
    }

    override fun setCompany(company: String) {
        binding.tvTradeDetailCompany.text = company
    }

    override fun setTicker(ticker: String) {
        binding.tvTradeDetailTicker.text = ticker
    }

    override fun setPrice(price: String) {
        binding.tvTradeDetailPrice.text = price
    }

    override fun setPriceTitle(priceTitle: String) {
        binding.tvTradeDetailPriceTitle.text = priceTitle
    }

    override fun setCount(count: String) {
        binding.tvTradeDetailCount.text = count
    }

    override fun setSum(sum: String) {
        binding.tvTradeDetailSum.text = sum
    }

    override fun setSumTitle(sumTitle: String) {
        binding.tvTradeDetailSumTitle.text = sumTitle
    }

    override fun setDate(date: String) {
        binding.tvTradeDetailDate.text = date
    }

    override fun setSubtype(type: String) {
        binding.tvTradeDetailType.text = type
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}