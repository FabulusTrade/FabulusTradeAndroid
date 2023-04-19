package ru.fabulus.fabulustrade.mvp.model.entity

import java.util.Date

data class AggregatedTrade(
    val idCompany: Int,
    val company: String,
    val count: Int,
    val companyImg: String,
    val dateLast: Date
)