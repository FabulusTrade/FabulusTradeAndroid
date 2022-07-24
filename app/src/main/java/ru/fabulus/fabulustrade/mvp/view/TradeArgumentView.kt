package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradeArgumentView  : BasePostView, MvpView {
    fun setRepostCount(text: String)
    fun setPostMenuSelf(argument: Argument)
    fun setPostMenuSomeone(argument: Argument, complaintList: List<Complaint>)
    fun setType(type: String)
    fun setCompany(company: String)
    fun setTicker(ticker: String)
    fun setPrice(price: String)
    fun setPriceTitle(priceTitle: String)
    fun setDate(date: String)
    fun setSubtype(type: String)
    fun setTradeType(type: TradeDetailFragment.TradeType)

}