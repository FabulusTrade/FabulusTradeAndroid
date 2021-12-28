package ru.fabulus.fabulustrade.mvp.model.entity

import java.util.*

class TradesByCompanyAggregated(
    var companyId: Int,
    var companyName: String,
    var companyLogo: String,
    var tradesCount: Int,
    var lastTrade: Date?
)