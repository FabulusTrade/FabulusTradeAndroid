package ru.wintrade.mvp.view.item

interface SubscriberTradeItemView {
    var pos: Int

    fun setAvatar(url: String)
    fun setNickname(nickname: String)
    fun setType(type: String)
    fun setCompany(company: String)
    fun setPrice(price: String)
    fun setSum(sum: String)
    fun setCount(count: String)
    fun setDate(date: String)
}