package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trade_detail.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.presenter.TradeDetailPresenter
import ru.wintrade.mvp.view.TradeDetailView
import ru.wintrade.ui.App

class TradeDetailFragment: MvpAppCompatFragment(), TradeDetailView {

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
    ): View? = inflater.inflate(R.layout.fragment_trade_detail, container, false)

    override fun init() {
        iv_trade_detail_close.setOnClickListener {
            presenter.closeClicked()
        }
    }

    override fun setName(traderName: String) {
        tv_trade_detail_trader_name.text = traderName
    }

    override fun setType(type: String) {
        tv_trade_detail_operation.text = type
    }

    override fun setCompany(company: String) {
        tv_trade_detail_company.text = company
    }

    override fun setTicker(ticker: String) {
        tv_trade_detail_ticker.text = ticker
    }

    override fun setPrice(price: String) {
        tv_trade_detail_price.text = price
    }

    override fun setPriceTitle(priceTitle: String) {
        tv_trade_detail_price_title.text = priceTitle
    }

    override fun setCount(count: String) {
        tv_trade_detail_count.text = count
    }

    override fun setSum(sum: String) {
        tv_trade_detail_sum.text = sum
    }

    override fun setSumTitle(sumTitle: String) {
        tv_trade_detail_sum_title.text = sumTitle
    }

    override fun setDate(date: String) {
        tv_trade_detail_date.text = date
    }

    override fun setSubtype(type: String) {
        tv_trade_detail_type.text = type
    }
}