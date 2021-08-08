package ru.wintrade.mvp.model.entity

import java.util.*

class TradesByCompany(
    var companyId: Int,
    var companyName: String,
    var companyLogo: String,
    var tradesCount: Int,
    var lastTrade: Date?
)