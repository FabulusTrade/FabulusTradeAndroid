package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTradeDetailBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.presenter.TradeDetailPresenter
import ru.fabulus.fabulustrade.mvp.view.TradeDetailView
import ru.fabulus.fabulustrade.ui.App

class TradeDetailFragment : MvpAppCompatFragment(), TradeDetailView {

    enum class Mode {
        TRADER_NO_ARGUMENT,
        TRADER_FILLING_ARGUMENT,
        TRADER_HAS_ARGUMENT
    }

    private var currentMode: Mode = Mode.TRADER_NO_ARGUMENT

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
        setMode(Mode.TRADER_NO_ARGUMENT)
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.linearShareArgumentsBegin.setOnClickListener {
            setMode(Mode.TRADER_FILLING_ARGUMENT)
        }
    }

    private fun setMode(mode: Mode) {
        when (mode) {
            Mode.TRADER_NO_ARGUMENT -> setTraderNoArgumentMode()
            Mode.TRADER_FILLING_ARGUMENT -> setTraderFillingArgumentMode()
            Mode.TRADER_HAS_ARGUMENT -> setTraderHasArgumentMode()
        }
        currentMode = mode
    }

    private fun setTraderNoArgumentMode() {
        binding.linearShareArgumentsBegin.visibility = View.VISIBLE
        binding.layoutArgumentTable.visibility = View.GONE
        binding.layoutPostText.visibility = View.GONE
        binding.layoutSharingPanel.visibility = View.GONE
    }

    private fun setTraderFillingArgumentMode() {
        binding.linearShareArgumentsBegin.visibility = View.GONE
        binding.layoutArgumentTable.visibility = View.VISIBLE
        binding.layoutPostText.visibility = View.VISIBLE
        binding.layoutSharingPanel.visibility = View.VISIBLE
        binding.linearShareHead.visibility = View.VISIBLE
        binding.ivTradeDetailClose.apply {
            visibility = View.INVISIBLE
            isClickable = false
        }
    }

    private fun setTraderHasArgumentMode() {
        binding.linearShareArgumentsBegin.visibility = View.VISIBLE
        binding.layoutArgumentTable.visibility = View.GONE
        binding.layoutPostText.visibility = View.GONE
        binding.layoutSharingPanel.visibility = View.GONE
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