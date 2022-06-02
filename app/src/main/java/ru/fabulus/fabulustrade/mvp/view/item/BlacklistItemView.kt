package ru.fabulus.fabulustrade.mvp.view.item

import ru.fabulus.fabulustrade.R

interface BlacklistItemView {
    var pos: Int
    fun setTraderId(id: String)
    fun setTraderName(name: String)
    fun setFollowersCount(count: Int)
    fun setTraderProfit(profit: String, textColor: Int)
    fun setBlacklistedAt(blacklistedAt: String)
    fun setTraderAvatar(avatar: String?)
    fun setProfitPositiveArrow()
    fun setProfitNegativeArrow()
}