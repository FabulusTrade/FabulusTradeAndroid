package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradeDetailView: MvpView {
    fun init()
    fun setName(traderName: String)
    fun setType(type: String)
    fun setCompany(company: String)
    fun setTicker(ticker: String)
    fun setPrice(price: String)
    fun setPriceTitle(priceTitle: String)
    fun setDate(date: String)
    fun setSubtype(type: String)
    fun setMode(mode: TradeDetailFragment.Mode)
    fun setTradeType(type: TradeDetailFragment.TradeType)
    fun setTakeProfit(takeProfit: Float)
    fun setStopLoss(stopLoss: Float)
    fun setProfit(profit: Float, precision: Int)
    fun setLoss(loss: Float, precision: Int)
    fun setArgumentText(text: String)
    fun showErrorMessage(message: String)
    fun setDealTerm(term: Float, precision: Int)
    fun updateListOfImages(
        images: List<CreatePostPresenter.ImageOfPost>,
        deletedImage: CreatePostPresenter.ImageOfPost?
    )

    fun showImagesAddedMessage(count: Int)
}