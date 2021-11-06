package ru.wintrade.mvp.presenter

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.view.TradeDetailView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TradeDetailPresenter(val trade: Trade) : MvpPresenter<TradeDetailView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        val dateFormat = SimpleDateFormat("dd.MM.yyyy / HH.mm", Locale.getDefault())
        val date = dateFormat.format(trade.date)

        trade.trader?.username?.let { viewState.setName(it) }
        viewState.setType(trade.operationType)
        viewState.setCompany(trade.company)
        viewState.setTicker(trade.ticker)
        viewState.setPrice(trade.price.toString())
        viewState.setPriceTitle("Цена, ${trade.currency}")
        viewState.setDate(date)
        viewState.setCount(trade.count.toString())
        viewState.setSum((trade.value).toString())
        viewState.setSumTitle("Сумма, ${trade.currency}")
        viewState.setSubtype(trade.subtype)
    }

    fun closeClicked() {
        router.exit()
    }
}